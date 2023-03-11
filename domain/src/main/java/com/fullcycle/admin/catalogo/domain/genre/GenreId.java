package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.Identifier;
import java.util.Objects;
import java.util.UUID;

public class GenreId extends Identifier {

  private final String value;

  private GenreId(String value) {
    Objects.requireNonNull(value);
    this.value = value;
  }

  public static GenreId unique() {
    return new GenreId(UUID.randomUUID().toString().toLowerCase());
  }

  public static GenreId from(final String anId) {
    return new GenreId(anId);
  }

  public static GenreId from(final UUID anId) {
    return new GenreId(anId.toString().toLowerCase());
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenreId that = (GenreId) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
