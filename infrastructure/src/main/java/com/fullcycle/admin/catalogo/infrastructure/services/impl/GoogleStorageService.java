package com.fullcycle.admin.catalogo.infrastructure.services.impl;

import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class GoogleStorageService implements StorageService {

  private final String bucket;
  private final Storage storage;

  public GoogleStorageService(String bucket, Storage storage) {
    this.bucket = bucket;
    this.storage = storage;
  }

  @Override
  public void store(String name, Resource resource) {
    BlobInfo blobInfo = BlobInfo.newBuilder(this.bucket, name)
        .setContentType(resource.getContentType()).setCrc32cFromHexString(resource.getChecksum())
        .build();

    this.storage.create(blobInfo, resource.getContent());

  }

  @Override
  public void deleteAll(Collection<String> names) {
    names.stream().map(name -> BlobId.of(this.bucket, name)).forEach(storage::delete);
  }

  @Override
  public Optional<Resource> get(String name) {
    return Optional.ofNullable(this.storage.get(BlobId.of(this.bucket, name))).map(
        blob -> Resource.with(blob.getCrc32cToHexString(), blob.getContent(), blob.getContentType(),
            name));

  }

  @Override
  public List<String> list(String prefix) {
    return StreamSupport.stream(
        this.storage.list(this.bucket, Storage.BlobListOption.prefix(prefix)).iterateAll()
            .spliterator(), false).map(BlobInfo::getBlobId).map(BlobId::getName).toList();

  }
}
