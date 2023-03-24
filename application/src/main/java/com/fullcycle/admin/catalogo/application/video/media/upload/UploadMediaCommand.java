package com.fullcycle.admin.catalogo.application.video.media.upload;

import com.fullcycle.admin.catalogo.domain.video.VideoResource;

public record UploadMediaCommand(
    String videoId,
    VideoResource resource
) {

  public static UploadMediaCommand with(String videoId, VideoResource resource) {
    return new UploadMediaCommand(videoId, resource);
  }

}
