package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.video.Video;

public record CreateVideoOutput(String id) {

  public static CreateVideoOutput create(String id) {
    return new CreateVideoOutput(id);
  }

  public static CreateVideoOutput create(Video video) {
    return new CreateVideoOutput(video.getId().getValue());
  }

}
