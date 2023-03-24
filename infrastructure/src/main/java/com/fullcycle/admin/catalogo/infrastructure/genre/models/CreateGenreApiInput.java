package com.fullcycle.admin.catalogo.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public record CreateGenreApiInput(
    @JsonProperty("name") String name,
    @JsonProperty("categories_id") Set<String> categories,
    @JsonProperty("is_active") Boolean isActive
) {

  public Set<String> categories() {
    return categories != null ? categories : Collections.emptySet();
  }

  public Boolean isActive() {
    return isActive != null ? isActive : true;
  }

}
