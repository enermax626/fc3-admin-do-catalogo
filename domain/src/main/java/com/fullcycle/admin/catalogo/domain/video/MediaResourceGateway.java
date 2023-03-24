package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.resource.Resource;
import java.util.Optional;

public interface MediaResourceGateway {

  AudioVideoMedia storeAudioVideo(VideoId anId, VideoResource aResource);

  ImageMedia storeImage(VideoId anId, VideoResource aResource);

  Optional<Resource> getResource(VideoId anId, VideoMediaType aType);

  void clearResources(VideoId anId);

}
