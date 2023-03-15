package com.fullcycle.admin.catalogo.infrastructure.genre;

import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
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
    if (this.genreRepository.existsById(anId.getValue())) {
      this.genreRepository.deleteById(anId.getValue());
    }
  }

  @Override
  public Optional<Genre> findById(GenreId anId) {
    return this.genreRepository.findById(anId.getValue()).map(GenreJpaEntity::toAggregate);
  }

  @Override
  public Genre update(Genre aGenre) {
    return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
  }

  @Override
  public Pagination<Genre> findAll(SearchQuery aQuery) {
    PageRequest request = PageRequest.of(aQuery.page(), aQuery.perPage(),
        Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort()));

    final var whereClause = Optional.ofNullable(aQuery.terms())
        .filter(terms -> !terms.isEmpty())
        .map(this::assembleSpecification)
        .orElse(null);

    final var results = this.genreRepository.findAll(whereClause, request);

    return new Pagination<>(
        results.getNumber(),
        results.getSize(),
        results.getTotalElements(),
        results.map(GenreJpaEntity::toAggregate).toList()
    );
  }

  private Specification<GenreJpaEntity> assembleSpecification(String terms) {
    return SpecificationUtils.like("name", terms);
  }
}
