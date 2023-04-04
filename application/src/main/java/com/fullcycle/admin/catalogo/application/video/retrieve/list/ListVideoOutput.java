package com.fullcycle.admin.catalogo.application.video.retrieve.list;

import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoPreview;
import java.time.Instant;

public record ListVideoOutput(String id, String title, String description, Instant createdAt,
                              Instant updatedAt) {

  public static ListVideoOutput from(Video aVideo) {
    return new ListVideoOutput(aVideo.getId().getValue(), aVideo.getTitle(),
        aVideo.getDescription(), aVideo.getCreatedAt(), aVideo.getUpdatedAt());
  }

  public static ListVideoOutput from(VideoPreview aVideo) {
    return new ListVideoOutput(aVideo.id().toString(), aVideo.title(), aVideo.description(),
        aVideo.createdAt(), aVideo.updatedAt());
  }

}
