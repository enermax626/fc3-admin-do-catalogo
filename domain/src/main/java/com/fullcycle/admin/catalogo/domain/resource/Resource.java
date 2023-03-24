package com.fullcycle.admin.catalogo.domain.resource;

import com.fullcycle.admin.catalogo.domain.ValueObject;
import java.util.Objects;

public class Resource extends ValueObject {

  private final String checksum;
  private final byte[] content;
  private final String contentType;
  private final String name;

  private Resource(String checksum, byte[] content, String contentType, String name) {
    this.content = Objects.requireNonNull(content);
    this.contentType = Objects.requireNonNull(contentType);
    this.checksum = Objects.requireNonNull(checksum);
    this.name = Objects.requireNonNull(name);
  }

  public static Resource with(String checksum, byte[] content, String contentType, String name) {
    return new Resource(checksum, content, contentType, name);
  }

  public String getChecksum() {
    return checksum;
  }

  public byte[] getContent() {
    return content;
  }

  public String getContentType() {
    return contentType;
  }

  public String getName() {
    return name;
  }

}
