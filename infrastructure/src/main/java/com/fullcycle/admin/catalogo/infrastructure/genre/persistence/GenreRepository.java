package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreRepository extends JpaRepository<GenreJpaEntity, String> {

  Page<GenreJpaEntity> findAll(Specification<GenreJpaEntity> whereClause, Pageable page);

  @Query("SELECT c.id FROM GenreJpaEntity c WHERE c.id IN :ids")
  List<String> existsByIds(@Param("ids") List<String> ids);
}
