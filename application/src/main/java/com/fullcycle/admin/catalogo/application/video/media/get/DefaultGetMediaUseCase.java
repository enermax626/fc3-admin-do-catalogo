package com.fullcycle.admin.catalogo.application.video.media.get;

import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoId;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;

public class DefaultGetMediaUseCase extends GetMediaUseCase {

  private final MediaResourceGateway mediaResourceGateway;

  public DefaultGetMediaUseCase(MediaResourceGateway mediaResourceGateway) {
    this.mediaResourceGateway = mediaResourceGateway;
  }

  @Override
  public GetMediaOutput execute(GetMediaCommand input) {
    VideoId anId = VideoId.from(input.videoId());

    Resource resource = mediaResourceGateway.getResource(anId, input.mediaType()).orElseThrow(
        () -> NotFoundException.with(
            new Error("Resource %s not found for video %s".formatted(input.mediaType().name(),
                anId.getValue()))));

    return GetMediaOutput.with(resource);
  }
}
