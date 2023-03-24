package com.fullcycle.admin.catalogo.infrastructure.video.persistence;


import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoId;
import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "video")
@Entity
public class VideoJpaEntity {

  @Id
  private String id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "launched_at", nullable = false)
  private Integer launchedAt;

  @Column(name = "duration", nullable = false, precision = 2)
  private Double duration;

  @Column(name = "rating", nullable = false)
  private Rating rating;

  @Column(name = "opened", nullable = false)
  private Boolean opened;

  @Column(name = "published", nullable = false)
  private Boolean published;

  @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant updatedAt;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "video_id")
  private AudioVideoMediaJpaEntity video;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "trailer_id")
  private AudioVideoMediaJpaEntity trailer;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "banner_id")
  private ImageMediaJpaEntity banner;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "thumbnail_id")
  private ImageMediaJpaEntity thumbnail;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "thumbnail_half_id")
  private ImageMediaJpaEntity thumbnailHalf;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<VideoCategoryJpaEntity> categories;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<VideoGenreJpaEntity> genres;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<VideoCastMemberJpaEntity> castMembers;


  public VideoJpaEntity() {
  }

  private VideoJpaEntity(String id, String title, String description, Integer launchedAt,
      Double duration, Rating rating, Boolean opened, Boolean published, Instant createdAt,
      Instant updatedAt, AudioVideoMediaJpaEntity video, AudioVideoMediaJpaEntity trailer,
      ImageMediaJpaEntity banner, ImageMediaJpaEntity thumbnail,
      ImageMediaJpaEntity thumbnailHalf) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.launchedAt = launchedAt;
    this.duration = duration;
    this.rating = rating;
    this.opened = opened;
    this.published = published;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.video = video;
    this.trailer = trailer;
    this.banner = banner;
    this.thumbnail = thumbnail;
    this.thumbnailHalf = thumbnailHalf;
    this.categories = new HashSet<>(3);
    this.genres = new HashSet<>(3);
    this.castMembers = new HashSet<>(3);
  }

  public static VideoJpaEntity from(Video aVideo) {
    var entity = new VideoJpaEntity(aVideo.getId().getValue(), aVideo.getTitle(),
        aVideo.getDescription(), aVideo.getLaunchedAt().getValue(), aVideo.getDuration(),
        aVideo.getRating(), aVideo.getOpened(), aVideo.getPublished(), aVideo.getCreatedAt(),
        aVideo.getUpdatedAt(), aVideo.getVideo().map(AudioVideoMediaJpaEntity::from).orElse(null),
        aVideo.getTrailer().map(AudioVideoMediaJpaEntity::from).orElse(null),
        aVideo.getBanner().map(ImageMediaJpaEntity::from).orElse(null),
        aVideo.getThumbnail().map(ImageMediaJpaEntity::from).orElse(null),
        aVideo.getThumbnailHalf().map(ImageMediaJpaEntity::from).orElse(null));

    aVideo.getCategories().forEach(entity::addCategory);
    aVideo.getGenres().forEach(entity::addGenre);
    aVideo.getMembers().forEach(entity::addCastMember);

    return entity;
  }

  public void addCategory(CategoryId categoryId) {
    this.categories.add(VideoCategoryJpaEntity.with(categoryId, this));
  }

  public void addGenre(GenreId genreId) {
    this.genres.add(VideoGenreJpaEntity.with(genreId, this));
  }

  public void addCastMember(CastMemberId castMemberId) {
    this.castMembers.add(VideoCastMemberJpaEntity.with(castMemberId, this));
  }

  public Set<VideoCategoryJpaEntity> getCategories() {
    return categories;
  }

  public Set<VideoGenreJpaEntity> getGenres() {
    return genres;
  }

  public Set<VideoCastMemberJpaEntity> getCastMembers() {
    return castMembers;
  }

  public Video toAggregate() {
    return Video.with(VideoId.from(this.id), this.title, this.description, Year.of(this.launchedAt),
        this.duration, this.rating, this.opened, this.published, this.createdAt, this.updatedAt,
        Optional.ofNullable(this.banner).map(ImageMediaJpaEntity::toAggregate).orElse(null),
        Optional.ofNullable(this.thumbnail).map(ImageMediaJpaEntity::toAggregate).orElse(null),
        Optional.ofNullable(this.thumbnailHalf).map(ImageMediaJpaEntity::toAggregate).orElse(null),
        Optional.ofNullable(this.trailer).map(AudioVideoMediaJpaEntity::toAggregate).orElse(null),
        Optional.ofNullable(this.video).map(AudioVideoMediaJpaEntity::toAggregate).orElse(null),
        getCategories().stream().map(it -> CategoryId.from(it.getId().getCategoryId()))
            .collect(Collectors.toSet()),
        getGenres().stream().map(it -> GenreId.from(it.getId().getGenreId()))
            .collect(Collectors.toSet()),
        getCastMembers().stream().map(it -> CastMemberId.from(it.getId().getCastMemberId()))
            .collect(Collectors.toSet()));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public Integer getLaunchedAt() {
    return launchedAt;
  }

  public void setLaunchedAt(Integer launchedAt) {
    this.launchedAt = launchedAt;
  }

  public Double getDuration() {
    return duration;
  }

  public void setDuration(Double duration) {
    this.duration = duration;
  }

  public Rating getRating() {
    return rating;
  }

  public void setRating(Rating rating) {
    this.rating = rating;
  }

  public Boolean getOpened() {
    return opened;
  }

  public void setOpened(Boolean opened) {
    this.opened = opened;
  }

  public Boolean getPublished() {
    return published;
  }

  public void setPublished(Boolean published) {
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

  public AudioVideoMediaJpaEntity getVideo() {
    return video;
  }

  public void setVideo(AudioVideoMediaJpaEntity video) {
    this.video = video;
  }

  public AudioVideoMediaJpaEntity getTrailer() {
    return trailer;
  }

  public void setTrailer(AudioVideoMediaJpaEntity trailer) {
    this.trailer = trailer;
  }

  public ImageMediaJpaEntity getBanner() {
    return banner;
  }

  public void setBanner(ImageMediaJpaEntity banner) {
    this.banner = banner;
  }

  public ImageMediaJpaEntity getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(ImageMediaJpaEntity thumbnail) {
    this.thumbnail = thumbnail;
  }

  public ImageMediaJpaEntity getThumbnailHalf() {
    return thumbnailHalf;
  }

  public void setThumbnailHalf(ImageMediaJpaEntity thumbnailHalf) {
    this.thumbnailHalf = thumbnailHalf;
  }
}
