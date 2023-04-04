package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Table(name = "video_categories")
@Entity
public class VideoCategoryJpaEntity {

  @EmbeddedId
  private VideoCategoryID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("videoId")
  private VideoJpaEntity video;

  public VideoCategoryJpaEntity() {
  }

  private VideoCategoryJpaEntity(VideoCategoryID id, VideoJpaEntity video) {
    this.id = id;
    this.video = video;
  }

  public static VideoCategoryJpaEntity with(CategoryId id, VideoJpaEntity video) {
    return new VideoCategoryJpaEntity(
        VideoCategoryID.with(video.getId(), id.getValue()), video);
  }

  public VideoCategoryID getId() {
    return id;
  }

  public void setId(VideoCategoryID id) {
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
    VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
