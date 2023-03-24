package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import java.util.Objects;

public class VideoId extends Identifier {

  private final String value;

  private VideoId(String value) {
    Objects.requireNonNull(value);
    this.value = value;
  }

  public static VideoId unique(){
    return new VideoId(IdUtils.generate());
  }

  public static VideoId from(final String anId){
    return new VideoId(anId.toLowerCase());
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VideoId that = (VideoId) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
