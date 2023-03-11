package com.fullcycle.admin.catalogo.application.genre.delete;

import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

  private final GenreGateway genreGateway;

  public DefaultDeleteGenreUseCase(GenreGateway genreGateway) {
    this.genreGateway = genreGateway;
  }

  @Override
  public void execute(String genreId) {
    this.genreGateway.deleteById(GenreId.from(genreId));
  }
}
