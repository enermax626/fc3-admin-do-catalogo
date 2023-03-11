package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;

public class DefaultGetGenreUseCase extends GetGenreUseCase {

  private final GenreGateway genreGateway;

  public DefaultGetGenreUseCase(GenreGateway genreGateway) {
    this.genreGateway = genreGateway;
  }

  @Override
  public GenreOutput execute(String id) {
    GenreId genreId = GenreId.from(id);
    Genre genre = genreGateway.findById(genreId)
        .orElseThrow(() -> NotFoundException.with(Genre.class, genreId));

    return GenreOutput.from(genre);
  }
}

