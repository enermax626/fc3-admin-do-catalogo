package com.fullcycle.admin.catalogo.application.genre.update;

import java.util.List;

public record UpdateGenreCommand(
    String id,
    String name,
    boolean isActive,
    List<String> categories
) {
  public static UpdateGenreCommand with(String id, String name, Boolean isActive,
      List<String> categories) {
    return new UpdateGenreCommand(id, name, isActive == null || isActive, categories == null ? List.of() : categories);
  }

}
