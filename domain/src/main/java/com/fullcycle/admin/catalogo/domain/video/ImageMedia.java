package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.ValueObject;
import java.util.Objects;

public class ImageMedia extends ValueObject {

  private final String id;
  private final String checksum;
  private final String name;
  private final String location;

  private ImageMedia(String id, String checksum, String name, String location) {
    this.id = id;
    this.checksum = Objects.requireNonNull(checksum);
    this.name = Objects.requireNonNull(name);
    this.location = Objects.requireNonNull(location);
  }

  public static ImageMedia with(String id, String checksum, String name, String location){
    return new ImageMedia(id, checksum, name, location);
  }

  public String id() {
    return id;
  }
  public String checksum() {
    return checksum;
  }

  public String name() {
    return name;
  }

  public String location() {
    return location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageMedia that = (ImageMedia) o;
    return checksum.equals(that.checksum) && location.equals(that.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checksum, location);
  }
}
