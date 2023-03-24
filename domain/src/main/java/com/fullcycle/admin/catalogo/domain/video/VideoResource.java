package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.ValueObject;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import java.util.Objects;

public class VideoResource extends ValueObject {

  private final Resource resource;
  private final  VideoMediaType type;

  public VideoResource(Resource resource, VideoMediaType type) {
    this.resource = Objects.requireNonNull(resource);
    this.type = Objects.requireNonNull(type);
  }

  public static VideoResource with(Resource resource, VideoMediaType type) {
    return new VideoResource(resource, type);
  }

  public Resource getResource() {
    return resource;
  }

  public VideoMediaType getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VideoResource that = (VideoResource) o;
    return type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }
}
