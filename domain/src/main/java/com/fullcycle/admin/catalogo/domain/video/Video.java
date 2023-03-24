package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.domain.events.VideoMidiaCreated;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Video extends AggregateRoot<VideoId> {

  private String title;
  private String description;
  private Year launchedAt;
  private double duration;
  private Rating rating;

  private boolean opened;
  private boolean published;

  private Instant createdAt;
  private Instant updatedAt;

  private ImageMedia banner;
  private ImageMedia thumbnail;
  private ImageMedia thumbnailHalf;

  private AudioVideoMedia trailer;
  private AudioVideoMedia video;

  private Set<CategoryId> categories;
  private Set<GenreId> genres;
  private Set<CastMemberId> members;

  private Video(VideoId anId, String title, String description, Year launchedAt, double duration,
      Rating rating, boolean opened, boolean published, Instant createdAt, Instant updatedAt,
      ImageMedia banner, ImageMedia thumbnail, ImageMedia thumbnailHalf, AudioVideoMedia trailer,
      AudioVideoMedia video, Set<CategoryId> categories, Set<GenreId> genres,
      Set<CastMemberId> members, List<DomainEvent> domainEvents) {
    super(anId, domainEvents);
    this.title = title;
    this.description = description;
    this.launchedAt = launchedAt;
    this.duration = duration;
    this.rating = rating;
    this.opened = opened;
    this.published = published;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.banner = banner;
    this.thumbnail = thumbnail;
    this.thumbnailHalf = thumbnailHalf;
    this.trailer = trailer;
    this.video = video;
    this.categories = categories;
    this.genres = genres;
    this.members = members;
  }

  public static Video newVideo(String title, String description, Year launchedAt, double duration,
      Rating rating, boolean opened, boolean published, Set<CategoryId> categories,
      Set<GenreId> genres, Set<CastMemberId> castMembers) {
    final var now = Instant.now();

    return new Video(VideoId.unique(), title, description, launchedAt, duration, rating, opened,
        published, now, now, null, null, null, null, null, categories, genres, castMembers, null);
  }

  public static Video with(Video video) {
    return new Video(video.getId(), video.getTitle(), video.getDescription(), video.getLaunchedAt(),
        video.getDuration(), video.getRating(), video.getOpened(), video.getPublished(),
        video.getCreatedAt(), video.getUpdatedAt(), video.getBanner().orElse(null),
        video.getThumbnail().orElse(null), video.getThumbnailHalf().orElse(null),
        video.getTrailer().orElse(null), video.getVideo().orElse(null),
        new HashSet<>(video.getCategories()), new HashSet<>(video.getGenres()),
        new HashSet<>(video.getMembers()), video.getDomainEvents());
  }

  public static Video with(
      VideoId id,
      String title,
      String description,
      Year launchedAt,
      double duration,
      Rating rating,
      boolean opened,
      boolean published,
      Instant createdAt,
      Instant updatedAt,
      ImageMedia banner,
      ImageMedia thumbnail,
      ImageMedia thumbnailHalf,
      AudioVideoMedia trailer,
      AudioVideoMedia video,
      Set<CategoryId> categories,
      Set<GenreId> genres,
      Set<CastMemberId> members) {
    return new Video(id, title, description, launchedAt, duration, rating, opened, published,
        createdAt, updatedAt, banner, thumbnail, thumbnailHalf, trailer, video, categories, genres,
        members, null);
  }

  public Video update(String title, String description, Year launchedAt, double duration,
      Rating rating, boolean opened, boolean published, Set<CategoryId> categories,
      Set<GenreId> genres, Set<CastMemberId> castMembers) {
    this.title = title;
    this.description = description;
    this.launchedAt = launchedAt;
    this.duration = duration;
    this.rating = rating;
    this.opened = opened;
    this.published = published;
    this.updatedAt = Instant.now();
    this.setCategories(categories);
    this.setGenres(genres);
    this.setMembers(castMembers);

    return this;
  }

  public Set<CategoryId> getCategories() {
    return categories != null ? Collections.unmodifiableSet(categories) : new HashSet<>();
  }

  public Set<GenreId> getGenres() {
    return genres != null ? Collections.unmodifiableSet(genres) : new HashSet<>();
  }

  public Set<CastMemberId> getMembers() {
    return members != null ? Collections.unmodifiableSet(members) : new HashSet<>();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Year getLaunchedAt() {
    return launchedAt;
  }

  public void setLaunchedAt(Year launchedAt) {
    this.launchedAt = launchedAt;
  }

  public double getDuration() {
    return duration;
  }

  public void setDuration(double duration) {
    this.duration = duration;
  }

  public Rating getRating() {
    return rating;
  }

  public void setRating(Rating rating) {
    this.rating = rating;
  }

  public boolean getOpened() {
    return opened;
  }

  public void setOpened(boolean opened) {
    this.opened = opened;
  }

  public boolean getPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Optional<ImageMedia> getBanner() {
    return Optional.ofNullable(banner);
  }

  public Video updateBannerMedia(ImageMedia banner) {
    this.banner = banner;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Optional<ImageMedia> getThumbnail() {
    return Optional.ofNullable(thumbnail);
  }

  public Video updateThumbnailMedia(ImageMedia thumbnail) {
    this.thumbnail = thumbnail;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Optional<ImageMedia> getThumbnailHalf() {
    return Optional.ofNullable(thumbnail);
  }

  public Video updateThumbnailHalfMedia(ImageMedia thumbnailHalf) {
    this.thumbnailHalf = thumbnailHalf;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Optional<AudioVideoMedia> getTrailer() {
    return Optional.ofNullable(trailer);
  }

  public Video updateTrailerMedia(AudioVideoMedia trailer) {
    this.trailer = trailer;
    this.updatedAt = InstantUtils.now();
    onAudioVideoMediaUpdated(trailer);
    return this;
  }

  public Optional<AudioVideoMedia> getVideo() {
    return Optional.ofNullable(video);
  }

  public Video updateVideoMedia(AudioVideoMedia video) {
    this.video = video;
    this.updatedAt = InstantUtils.now();

    onAudioVideoMediaUpdated(video);
    return this;
  }

  private void onAudioVideoMediaUpdated(AudioVideoMedia media) {
    if (media != null && media.isPendingEncode()) {
      this.registerEvent(new VideoMidiaCreated(this.getId().getValue(), media.rawLocation()));
    }
  }

  private void setCategories(Set<CategoryId> categories) {
    this.categories = categories != null ? categories : Collections.emptySet();
  }

  private void setGenres(Set<GenreId> genres) {
    this.genres = genres != null ? genres : Collections.emptySet();
  }

  private void setMembers(Set<CastMemberId> members) {
    this.members = members != null ? members : Collections.emptySet();
  }

  @Override
  public void validate(ValidationHandler validationHander) {
    new VideoValidator(this, validationHander).validate();
  }

  public Video processing(VideoMediaType aType) {
    if (VideoMediaType.VIDEO == aType) {
      getVideo().ifPresent(media -> updateVideoMedia(media.processing()));
    } else if (VideoMediaType.TRAILER == aType) {
      getTrailer().ifPresent(media -> updateTrailerMedia(media.processing()));
    }
    return this;
  }

  public Video completed(VideoMediaType aType, String encodedPath) {
    if (VideoMediaType.VIDEO == aType) {
      getVideo().ifPresent(media -> updateVideoMedia(media.completed(encodedPath)));
    } else if (VideoMediaType.TRAILER == aType) {
      getTrailer().ifPresent(media -> updateTrailerMedia(media.completed(encodedPath)));
    }
    return this;
  }
}
