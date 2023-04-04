package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VideoGenreID implements Serializable {

  private static final long serialVersionUID = 4930976135541609103L;
  @Column(name = "video_id", nullable = false)
  private String videoId;

  @Column(name = "genre_id", nullable = false)
  private String genreId;

  public VideoGenreID() {
  }

  private VideoGenreID(String videoId, String genreId) {
    this.videoId = videoId;
    this.genreId = genreId;
  }

  public static VideoGenreID with(String videoId, String genreId) {
    return new VideoGenreID(videoId, genreId);
  }

  public String getVideoId() {
    return videoId;
  }

  public void setVideoId(String videoId) {
    this.videoId = videoId;
  }

  public String getGenreId() {
    return genreId;
  }

  public void setGenreId(String genreId) {
    this.genreId = genreId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VideoGenreID that = (VideoGenreID) o;
    return Objects.equals(videoId, that.videoId) && Objects.equals(genreId,
        that.genreId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(videoId, genreId);
  }
}
