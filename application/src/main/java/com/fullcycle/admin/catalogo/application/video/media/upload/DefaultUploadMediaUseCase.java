package com.fullcycle.admin.catalogo.application.video.media.upload;

import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoId;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase {

  private final MediaResourceGateway mediaResourceGateway;
  private final VideoGateway videoGateway;

  public DefaultUploadMediaUseCase(MediaResourceGateway mediaResourceGateway,
      VideoGateway videoGateway) {
    this.mediaResourceGateway = mediaResourceGateway;
    this.videoGateway = videoGateway;
  }

  @Override
  public UploadMediaOutput execute(UploadMediaCommand input) {
    final var anId = VideoId.from(input.videoId());
    final var aResource = input.resource();

    final var aVideo = videoGateway.findById(anId).orElseThrow(
        () -> NotFoundException.with(new Error("Video %s not found".formatted(anId.getValue()))));

    switch (aResource.getType()) {
      case VIDEO:
        aVideo.updateVideoMedia(mediaResourceGateway.storeAudioVideo(anId, aResource));
        break;
      case TRAILER:
        aVideo.updateTrailerMedia(mediaResourceGateway.storeAudioVideo(anId, aResource));
        break;
      case BANNER:
        aVideo.updateBannerMedia(mediaResourceGateway.storeImage(anId, aResource));
        break;
      case THUMBNAIL:
        aVideo.updateThumbnailMedia(mediaResourceGateway.storeImage(anId, aResource));
        break;
      case THUMBNAIL_HALF:
        aVideo.updateThumbnailHalfMedia(mediaResourceGateway.storeImage(anId, aResource));
        break;
    }

    return UploadMediaOutput.with(videoGateway.update(aVideo), aResource.getType());
  }
}
