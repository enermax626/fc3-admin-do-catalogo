package com.fullcycle.admin.catalogo.application.video.retrieve.get;

import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoId;

public class DefaultGetVideoUseCase extends
    GetVideoUseCase {

  private final VideoGateway videoGateway;

  public DefaultGetVideoUseCase(VideoGateway videoGateway) {
    this.videoGateway = videoGateway;
  }

  @Override
  public GetVideoOutput execute(String input) {
    VideoId videoId = VideoId.from(input);
    return this.videoGateway.findById(videoId)
        .map(GetVideoOutput::from)
        .orElseThrow(() -> NotFoundException.with(Video.class, videoId));

  }
}
