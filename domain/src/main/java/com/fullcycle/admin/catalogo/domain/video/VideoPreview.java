package com.fullcycle.admin.catalogo.domain.video;

import java.time.Instant;
import java.util.UUID;

public record VideoPreview(String id, String title, String description, Instant createdAt,
                           Instant updatedAt) {

  public VideoPreview(Video aVideo) {
    this(aVideo.getId().getValue(), aVideo.getTitle(), aVideo.getDescription(),
        aVideo.getCreatedAt(), aVideo.getUpdatedAt());
  }

}
