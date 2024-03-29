package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VideoCastMemberID implements Serializable {

  private static final long serialVersionUID = 8012990178951757815L;
  @Column(name = "video_id", nullable = false)
  private String videoId;

  @Column(name = "cast_member_id", nullable = false)
  private String castMemberId;

  public VideoCastMemberID() {
  }

  private VideoCastMemberID(String videoId, String castMemberId) {
    this.videoId = videoId;
    this.castMemberId = castMemberId;
  }

  public static VideoCastMemberID with(String videoId, String castMemberId) {
    return new VideoCastMemberID(videoId, castMemberId);
  }

  public String getVideoId() {
    return videoId;
  }

  public void setVideoId(String videoId) {
    this.videoId = videoId;
  }

  public String getCastMemberId() {
    return castMemberId;
  }

  public void setCastMemberId(String castMemberId) {
    this.castMemberId = castMemberId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VideoCastMemberID that = (VideoCastMemberID) o;
    return Objects.equals(videoId, that.videoId) && Objects.equals(castMemberId,
        that.castMemberId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(videoId, castMemberId);
  }
}
