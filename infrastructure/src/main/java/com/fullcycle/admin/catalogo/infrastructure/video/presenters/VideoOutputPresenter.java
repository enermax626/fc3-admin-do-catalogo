package com.fullcycle.admin.catalogo.infrastructure.video.presenters;

import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoOutput;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.infrastructure.video.models.AudioVideoMediaApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.ImageMediaApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoApiOutput;

public interface VideoOutputPresenter {

  static VideoApiOutput present(GetVideoOutput aVideo) {
    return new VideoApiOutput(
        aVideo.id(),
        aVideo.title(),
        aVideo.description(),
        aVideo.launchedAt(),
        aVideo.duration(),
        aVideo.opened(),
        aVideo.published(),
        aVideo.rating(),
        aVideo.createdAt(),
        aVideo.updatedAt(),
        present(aVideo.banner()),
        present(aVideo.thumbnail()),
        present(aVideo.thumbnailHalf()),
        present(aVideo.video()),
            present(aVideo.trailer()),
        aVideo.categories(),
        aVideo.genres(),
        aVideo.members()
    );
  }

  static AudioVideoMediaApiOutput present(AudioVideoMedia video) {
    if(video == null) return null;

    return new AudioVideoMediaApiOutput(
        video.id(),
        video.checksum(),
        video.name(),
        video.rawLocation(),
        video.encodedLocation(),
        video.status()
    );
  }

  static ImageMediaApiOutput present(ImageMedia banner) {
    if(banner == null) return null;

    return new ImageMediaApiOutput(
        banner.id(),
        banner.checksum(),
        banner.name(),
        banner.location()
    );
  }

  static UpdateVideoApiOutput present(UpdateVideoOutput result) {
    return new UpdateVideoApiOutput(
        result.id()
    );
  }
}
