package com.fullcycle.admin.catalogo.domain.video;

import java.util.Arrays;
import java.util.Optional;

public enum VideoMediaType {
  TRAILER, VIDEO, BANNER, THUMBNAIL, THUMBNAIL_HALF;

  public static Optional<VideoMediaType> of(String value) {
    return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(value)).findFirst();
  }
  }
