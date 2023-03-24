package com.fullcycle.admin.catalogo.application.video.media.get;

public record GetMediaCommand(
    String videoId,
    String mediaType
) {

  public GetMediaCommand with(String videoId, String mediaType) {
    return new GetMediaCommand(videoId, mediaType);
  }

}
