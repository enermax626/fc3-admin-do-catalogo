package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import java.util.Optional;
import java.util.Set;

public interface GenreGateway {

  Genre create(Genre aGenre);

  void deleteById(GenreId anId);

  Optional<Genre> findById(GenreId anId);

  Genre update(Genre aGenre);

  Pagination<Genre> findAll(SearchQuery aQuery);

  public Set<GenreId> existsByIds(Iterable<GenreId> categoriesIds);

}
