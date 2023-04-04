package com.fullcycle.admin.catalogo.application.video.update;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoId;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import com.fullcycle.admin.catalogo.domain.video.VideoResource;
import java.time.Year;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {

  private final VideoGateway videoGateway;
  private final CategoryGateway categoryGateway;
  private final CastMemberGateway castMemberGateway;
  private final GenreGateway genreGateway;

  private final MediaResourceGateway mediaResourceGateway;


  public DefaultUpdateVideoUseCase(VideoGateway videoGateway, CategoryGateway categoryGateway,
      CastMemberGateway castMemberGateway, GenreGateway genreGateway,
      MediaResourceGateway mediaResourceGateway) {
    this.videoGateway = videoGateway;
    this.categoryGateway = categoryGateway;
    this.castMemberGateway = castMemberGateway;
    this.genreGateway = genreGateway;
    this.mediaResourceGateway = mediaResourceGateway;
  }

  @Override
  public UpdateVideoOutput execute(UpdateVideoCommand input) {
    VideoId anId = VideoId.from(input.id());
    final var rating = Rating.of(input.rating()).orElse(null);
    final var launchedAt = input.launchedAt() != null ? Year.of(input.launchedAt()) : null;
    final var categories = toIdentifier(input.categories(), CategoryId::from);
    final var genres = toIdentifier(input.genres(), GenreId::from);
    final var members = toIdentifier(input.members(), CastMemberId::from);

    Video aVideo = this.videoGateway.findById(anId).orElseThrow(notFoundException(anId));

    final var notification = Notification.create();
    notification.append(validateCategories(categories));
    notification.append(validateGenres(genres));
    notification.append(validateMembers(members));

    aVideo.update(input.title(), input.description(), launchedAt, input.duration(), rating,
        input.opened(), input.published(), categories, genres, members);

    aVideo.validate(notification);

    if (notification.hasError()) {
      throw new NotificationException("Could not update aggregate", notification);
    }

    return UpdateVideoOutput.from(update(input, aVideo));
  }

  private Video update(UpdateVideoCommand input, Video aVideo) {
    final var anId = aVideo.getId();
    try {
      AudioVideoMedia video = input.getVideo().map(r -> mediaResourceGateway.storeAudioVideo(anId,
          VideoResource.with(r, VideoMediaType.VIDEO))).orElse(null);
      AudioVideoMedia trailer = input.getTrailer().map(
          r -> mediaResourceGateway.storeAudioVideo(anId,
              VideoResource.with(r, VideoMediaType.TRAILER))).orElse(null);
      ImageMedia thumbnail = input.getThumbnail().map(r -> mediaResourceGateway.storeImage(anId,
          VideoResource.with(r, VideoMediaType.THUMBNAIL))).orElse(null);
      ImageMedia banner = input.getBanner().map(
              r -> mediaResourceGateway.storeImage(anId, VideoResource.with(r, VideoMediaType.BANNER)))
          .orElse(null);
      ImageMedia thumbnailHalf = input.getThumbnailHalf().map(
          r -> mediaResourceGateway.storeImage(anId,
              VideoResource.with(r, VideoMediaType.THUMBNAIL_HALF))).orElse(null);

      return this.videoGateway.update(
          aVideo
              .updateVideoMedia(video)
              .updateTrailerMedia(trailer)
              .updateThumbnailMedia(thumbnail)
              .updateBannerMedia(banner)
              .updateThumbnailHalfMedia(thumbnailHalf)
      );
    } catch (Exception e) {
      throw InternalErrorException.with(
          "Could not update aggregate video with id: %s".formatted(anId), e);
    }
  }

  private ValidationHandler validateMembers(Set<CastMemberId> members) {
    return validateAggregate("CastMember", members, castMemberGateway::existsByIds);
  }

  private ValidationHandler validateGenres(Set<GenreId> genres) {
    return validateAggregate("Genre", genres, genreGateway::existsByIds);
  }

  private ValidationHandler validateCategories(Set<CategoryId> categories) {
    return validateAggregate("Category", categories, categoryGateway::existsByIds);
  }

  private <T extends Identifier> ValidationHandler validateAggregate(String aggregateName,
      Set<T> aggregateIds, Function<Set<T>, Set<T>> existsByIds) {
    final var notification = Notification.create();
    if (aggregateIds == null || aggregateIds.isEmpty()) {
      return notification;
    }
    final var retrievedIds = existsByIds.apply(aggregateIds);
    if (aggregateIds.size() != retrievedIds.size()) {
      final var missingIds = new ArrayList<>(aggregateIds);
      missingIds.removeAll(retrievedIds);

      String missingIdsText = missingIds.stream().map(Identifier::getValue)
          .collect(Collectors.joining(","));
      notification.append(new Error(
          "Some %s were not found. ids: %s".formatted(aggregateName,
              missingIdsText)));
    }
    return notification;
  }

  private Supplier<NotFoundException> notFoundException(VideoId anId) {
    return () -> NotFoundException.with(Video.class, anId);
  }

  public <T> Set<T> toIdentifier(Set<String> ids, Function<String, T> mapper) {
    return ids.stream().map(mapper).collect(Collectors.toSet());
  }
}
