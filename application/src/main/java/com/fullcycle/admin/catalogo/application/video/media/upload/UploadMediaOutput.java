package com.fullcycle.admin.catalogo.application.video.media.upload;

import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;

public record UploadMediaOutput(String videoId, VideoMediaType mediaType) {

  public static UploadMediaOutput with(Video video, VideoMediaType mediaType) {
    return new UploadMediaOutput(video.getId().getValue(), mediaType);
  }

}
