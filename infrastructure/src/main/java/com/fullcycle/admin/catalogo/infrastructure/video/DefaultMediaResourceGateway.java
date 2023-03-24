package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoId;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import com.fullcycle.admin.catalogo.domain.video.VideoResource;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

  private final String fileNamePattern;
  private final String locationPattern;
  private final StorageService storageService;

  public DefaultMediaResourceGateway(StorageProperties storageProperties,
      StorageService storageService) {
    this.fileNamePattern = storageProperties.getFileNamePattern();
    this.locationPattern = storageProperties.getLocationPattern();
    this.storageService = storageService;
  }

  @Override
  public AudioVideoMedia storeAudioVideo(VideoId anId, VideoResource aVideoResource) {
    final String filePath = filePath(anId, aVideoResource.getType());
    final Resource resource = aVideoResource.getResource();

    this.storageService.store(filePath, resource);

    return AudioVideoMedia.with(resource.getChecksum(), resource.getName(), filePath);
  }


  @Override
  public ImageMedia storeImage(VideoId anId, VideoResource aVideoResource) {
    final String filePath = filePath(anId, aVideoResource.getType());
    final Resource resource = aVideoResource.getResource();

    this.storageService.store(filePath, resource);

    return ImageMedia.with(resource.getChecksum(), resource.getChecksum(), resource.getName(), filePath);
  }

  @Override
  public Optional <Resource> getResource(VideoId anId, VideoMediaType aType) {
    return this.storageService.get(filePath(anId, aType));
  }

  @Override
  public void clearResources(VideoId anId) {
    List<String> ids = this.storageService.list(folder(anId));
    this.storageService.deleteAll(ids);
  }

  private String fileName(VideoMediaType aType) {
    return fileNamePattern.replace("{type}", aType.name());
  }

  private String folder(VideoId anId) {
    return locationPattern.replace("{videoId}", anId.getValue());
  }

  private String filePath(VideoId anId, VideoMediaType aMediaType) {
    return folder(anId).concat("/").concat(fileName(aMediaType));
  }
}
