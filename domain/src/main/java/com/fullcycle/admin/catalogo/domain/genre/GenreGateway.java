package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import java.util.Optional;

public interface GenreGateway {

  Genre create(Genre aGenre);

  void deleteById(GenreId anId);

  Optional<Genre> findById(GenreId anId);

  Genre update(Genre aGenre);

  Pagination<Genre> findAll(SearchQuery aQuery);

}
