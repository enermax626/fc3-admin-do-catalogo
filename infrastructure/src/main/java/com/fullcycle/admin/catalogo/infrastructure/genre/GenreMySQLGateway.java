package com.fullcycle.admin.catalogo.infrastructure.genre;

import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class GenreMySQLGateway implements GenreGateway {

  private final GenreRepository genreRepository;

  public GenreMySQLGateway(GenreRepository genreRepository) {
    this.genreRepository = genreRepository;
  }

  @Override
  public Genre create(Genre aGenre) {
    return save(aGenre);

  }

  private Genre save(Genre aGenre) {
    return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
  }

  @Override
  public void deleteById(GenreId anId) {

  }

  @Override
  public Optional<Genre> findById(GenreId anId) {
    return Optional.empty();
  }

  @Override
  public Genre update(Genre aGenre) {
    return null;
  }

  @Override
  public Pagination<Genre> findAll(SearchQuery aQuery) {
    return null;
  }
}
