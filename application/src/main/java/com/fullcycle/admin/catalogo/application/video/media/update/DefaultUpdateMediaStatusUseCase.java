package com.fullcycle.admin.catalogo.application.video.media.update;

import static com.fullcycle.admin.catalogo.domain.video.MediaStatus.COMPLETED;
import static com.fullcycle.admin.catalogo.domain.video.MediaStatus.PENDING;
import static com.fullcycle.admin.catalogo.domain.video.MediaStatus.PROCESSING;

import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoId;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;

public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {

  private final VideoGateway videoGateway;

  public DefaultUpdateMediaStatusUseCase(VideoGateway videoGateway) {
    this.videoGateway = videoGateway;
  }

  @Override
  public void execute(UpdateMediaStatusCommand input) {
    final var anId = VideoId.from(input.videoId());
    final var anResourceId = input.resourceId();
    final var folder = input.folder();
    final var filename = input.fileName();

    Video aVideo = this.videoGateway.findById(anId).orElseThrow(
        () -> NotFoundException.with(new Error("Video %s not found".formatted(anId.getValue()))));

    final var encodedPath = "%s/%s".formatted(folder, filename);

    if (matches(anResourceId, aVideo.getVideo().orElse(null))) {
      updateVideo(VideoMediaType.VIDEO,input.status(), aVideo, encodedPath);
      this.videoGateway.update(aVideo);
    } else if (matches(anResourceId, aVideo.getTrailer().orElse(null))) {
      updateVideo(VideoMediaType.TRAILER,input.status(), aVideo, encodedPath);

    }

  }

  private static void updateVideo(VideoMediaType aType,MediaStatus status, Video aVideo, String encodedPath) {
    switch (status) {
      case PENDING:
        break;
      case PROCESSING:
        aVideo.processing(aType);
        break;
      case COMPLETED:
          aVideo.completed(aType, encodedPath);
        break;
    }
  }

  private boolean matches(String anResourceId, AudioVideoMedia media) {
    if(media == null) {
      return false;
    }

    return media.id().equals(anResourceId);
  }
}
