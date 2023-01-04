package com.fullcycle.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public record UpdateCategoryApiInput(
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("is_active") Boolean active
) {

  public static UpdateCategoryApiInput from(
      final String name,
      final String description,
      final Boolean active
  ) {
    return new UpdateCategoryApiInput(
        name,
        description,
        Objects.isNull(active) ? true : active
    );
  }

}
