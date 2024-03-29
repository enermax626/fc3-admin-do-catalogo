package com.fullcycle.admin.catalogo.application.genre.retrieve.list;

import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;

public class DefaultListGenreUseCase extends ListGenreUseCase {

  private final GenreGateway genreGateway;

  public DefaultListGenreUseCase(GenreGateway genreGateway) {
    this.genreGateway = genreGateway;
  }

  @Override
  public Pagination<GenreListOutput> execute(SearchQuery input) {
    return genreGateway.findAll(input).map(GenreListOutput::from);
  }
}
