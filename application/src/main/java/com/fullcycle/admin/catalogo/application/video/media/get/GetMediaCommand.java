package com.fullcycle.admin.catalogo.application.video.media.get;

import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;

public record GetMediaCommand(
    String videoId,
    VideoMediaType mediaType
) {

  public static GetMediaCommand with(String videoId, VideoMediaType mediaType) {
    return new GetMediaCommand(videoId, mediaType);
  }

}
