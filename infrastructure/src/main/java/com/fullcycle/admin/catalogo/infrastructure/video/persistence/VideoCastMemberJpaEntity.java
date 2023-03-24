package com.fullcycle.admin.catalogo.infrastructure.video.persistence;


import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Table(name = "video_cast_member")
@Entity
public class VideoCastMemberJpaEntity {

  @EmbeddedId
  private VideoCastMemberID id;

  @MapsId("videoId")
  @ManyToOne(fetch = FetchType.LAZY)
  private VideoJpaEntity video;

  public VideoCastMemberJpaEntity() {
  }

  private VideoCastMemberJpaEntity(VideoCastMemberID id, VideoJpaEntity video) {
    this.id = id;
    this.video = video;
  }

  public static VideoCastMemberJpaEntity with(CastMemberId castMemberId, VideoJpaEntity video) {
    return new VideoCastMemberJpaEntity(
        VideoCastMemberID.with(video.getId(), castMemberId.getValue()), video);
  }

  public VideoCastMemberID getId() {
    return id;
  }

  public void setId(VideoCastMemberID id) {
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
    VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(video, that.video);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, video);
  }
}
