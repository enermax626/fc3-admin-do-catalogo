package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import com.fullcycle.admin.catalogo.domain.video.VideoResource;
import java.time.Year;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class  DefaultCreateVideoUseCase extends
    CreateVideoUseCase {

  private final VideoGateway videoGateway;
  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;
  private final CastMemberGateway castMemberGateway;
  private final MediaResourceGateway mediaResourceGateway;

  public DefaultCreateVideoUseCase(VideoGateway videoGateway, CategoryGateway categoryGateway,
      GenreGateway genreGateway, CastMemberGateway castMemberGateway,
      MediaResourceGateway mediaResourceGateway) {
    this.videoGateway = videoGateway;
    this.categoryGateway = categoryGateway;
    this.genreGateway = genreGateway;
    this.castMemberGateway = castMemberGateway;
    this.mediaResourceGateway = mediaResourceGateway;
  }

  @Override
  public CreateVideoOutput execute(CreateVideoCommand input) {

    final var categories = toIdentifier(input.categories(), CategoryId::from);
    final var genres = toIdentifier(input.categories(), GenreId::from);
    final var members = toIdentifier(input.categories(), CastMemberId::from);

    final var notification = Notification.create();
    notification.append(validateCategories(categories, categoryGateway::existsByIds));
    notification.append(validateGenres(genres, genreGateway::existsByIds));
    notification.append(validateMembers(members, castMemberGateway::existsByIds));

    Video aVideo = Video.newVideo(input.title(), input.description(), input.launchedAt(),
        input.duration(),
        input.rating(), input.opened(), input.published(), categories, genres, members);

    aVideo.validate(notification);

    if(notification.hasError()) {
      throw new NotificationException("Could not create aggregate video", notification);
    }



    return CreateVideoOutput.create(create(input, aVideo));
  }

  private Video create(CreateVideoCommand input, Video aVideo) {
    final var anId = aVideo.getId();
    try {
      AudioVideoMedia video = input.getVideo()
          .map(r -> mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(r, VideoMediaType.VIDEO))).orElse(null);
      AudioVideoMedia trailer = input.getTrailer()
          .map(r -> mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(r, VideoMediaType.TRAILER))).orElse(null);
      ImageMedia thumbnail = input.getThumbnail()
          .map(r -> mediaResourceGateway.storeImage(anId, VideoResource.with(r, VideoMediaType.THUMBNAIL))).orElse(null);
      ImageMedia banner = input.getBanner().map(r -> mediaResourceGateway.storeImage(anId, VideoResource.with(r, VideoMediaType.BANNER)))
          .orElse(null);
      ImageMedia thumbnailHalf = input.getThumbnailHalf()
          .map(r -> mediaResourceGateway.storeImage(anId, VideoResource.with(r, VideoMediaType.THUMBNAIL_HALF))).orElse(null);

      return this.videoGateway.create(
          aVideo
              .updateVideoMedia(video)
              .updateTrailerMedia(trailer)
              .updateThumbnailMedia(thumbnail)
              .updateBannerMedia(banner)
              .updateThumbnailHalfMedia(thumbnailHalf));
    } catch (Exception e) {
      this.mediaResourceGateway.clearResources(anId);
      throw InternalErrorException.with("Could not create aggregate video with id: %s".formatted(anId), e);
    }
  }

  private ValidationHandler validateMembers(Set<CastMemberId> members,
      Function<Set<CastMemberId>, Set<CastMemberId>> existsByIds) {
    return validateAggregate("CastMember", members, existsByIds);
  }

  private ValidationHandler validateGenres(Set<GenreId> genres,
      Function<Set<GenreId>, Set<GenreId>> existsByIds) {
    return validateAggregate("Genre", genres, existsByIds);
  }

  private ValidationHandler validateCategories(Set<CategoryId> categories,
      Function<Set<CategoryId>, Set<CategoryId>> existsByIds) {
    return validateAggregate("Category", categories, existsByIds);
  }

  private <T extends Identifier> ValidationHandler validateAggregate(
      String aggregateName,
      Set<T> aggregateIds,
      Function<Set<T>, Set<T>> existsByIds) {
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
      notification.append(
          new Error("Some categories were not found. Aggregate:%s ids: %s".formatted(aggregateName,
              missingIdsText)));
    }
    return notification;
  }


  public <T> Set<T> toIdentifier(Set<String> ids, Function<String, T> mapper) {
    return ids.stream().map(mapper).collect(Collectors.toSet());
  }
}
