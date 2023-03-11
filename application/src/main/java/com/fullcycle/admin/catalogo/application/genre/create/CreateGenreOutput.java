package com.fullcycle.admin.catalogo.application.genre.create;

import com.fullcycle.admin.catalogo.domain.genre.Genre;

public record CreateGenreOutput(
    String id
) {

  public static CreateGenreOutput from(final String anId) {
    return new CreateGenreOutput(anId);
  }

  public static CreateGenreOutput from(final Genre genre){
    return new CreateGenreOutput(genre.getId().getValue());
  }

}
