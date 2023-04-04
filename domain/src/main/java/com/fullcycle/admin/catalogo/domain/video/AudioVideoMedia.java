package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.ValueObject;
import java.util.Objects;

public class AudioVideoMedia extends ValueObject {

  private final String id;
  private final String checksum;
  private final String name;
  private final String rawLocation;
  private final String encodedLocation;
  private final MediaStatus status;

  private AudioVideoMedia(String id, String checksum, String name, String rawLocation, String encodedLocation,
      MediaStatus status) {
    this.id = Objects.requireNonNull(id);
    this.checksum = checksum;
    this.name = Objects.requireNonNull(name);
    this.rawLocation = Objects.requireNonNull(rawLocation);
    this.encodedLocation = encodedLocation;
    this.status = status == null ? MediaStatus.PENDING : status;
  }

  public static AudioVideoMedia with(String checksum, String name, String rawLocation, String encodedLocation,
      MediaStatus status){
    return new AudioVideoMedia(checksum, checksum, name, rawLocation, encodedLocation, status);
  }


  public static AudioVideoMedia with(String checksum, String name, String rawLocation){
    return new AudioVideoMedia(checksum, checksum, name, rawLocation, null, null);
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

  public String rawLocation() {
    return rawLocation;
  }

  public String encodedLocation() {
    return encodedLocation;
  }

  public MediaStatus status() {
    return status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AudioVideoMedia that = (AudioVideoMedia) o;
    return id.equals(that.id) && rawLocation.equals(that.rawLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, rawLocation);
  }

  public AudioVideoMedia processing() {
    return AudioVideoMedia.with(id, name, rawLocation, encodedLocation, MediaStatus.PROCESSING);
  }

  public AudioVideoMedia completed(String encodedPath) {
    return AudioVideoMedia.with(id, name, rawLocation, encodedPath, MediaStatus.COMPLETED);
  }

  public boolean isPendingEncode() {
    return MediaStatus.PENDING.equals(status);
  }
}
