package com.fullcycle.admin.catalogo.application.genre.create;

import java.util.Set;

public record CreateGenreCommand(String name,
                                 boolean isActive,
                                 Set<String> categories) {

  public static CreateGenreCommand with(final String aName, final Boolean isActive,
      final Set<String> categories) {
    return new CreateGenreCommand(aName, isActive != null ? isActive : true, categories);
  }


}
