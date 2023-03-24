package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Table(name = "video_genre")
@Entity
public class VideoGenreJpaEntity {

  @EmbeddedId
  private VideoGenreID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("videoId")
  private VideoJpaEntity video;

  public VideoGenreJpaEntity() {
  }

  private VideoGenreJpaEntity(VideoGenreID id, VideoJpaEntity video) {
    this.id = id;
    this.video = video;
  }

  public static VideoGenreJpaEntity with(GenreId id, VideoJpaEntity video) {
    return new VideoGenreJpaEntity(VideoGenreID.with(video.getId(), id.getValue()),
        video);
  }

  public VideoGenreID getId() {
    return id;
  }

  public void setId(VideoGenreID id) {
    this.id = id;
  }

  public VideoJpaEntity getVideo() {
    return video;
  }

  public void setVideo(VideoJpaEntity video) {
    this.video = video;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VideoGenreJpaEntity that = (VideoGenreJpaEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(video, that.video);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, video);
  }
}
