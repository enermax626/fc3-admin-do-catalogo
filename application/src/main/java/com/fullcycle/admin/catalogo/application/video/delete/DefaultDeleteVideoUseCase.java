package com.fullcycle.admin.catalogo.application.video.delete;

import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoId;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

  private final VideoGateway videoGateway;

  private final MediaResourceGateway mediaResourceGateway;

  public DefaultDeleteVideoUseCase(VideoGateway videoGateway,
      MediaResourceGateway mediaResourceGateway) {
    this.videoGateway = videoGateway;
    this.mediaResourceGateway = mediaResourceGateway;
  }

  @Override
  public void execute(String anId) {
    videoGateway.findById(VideoId.from(anId)).ifPresent(video -> {
      videoGateway.deleteById(video.getId());
      mediaResourceGateway.clearResources(video.getId());
    });

  }
}
