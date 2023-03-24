package com.fullcycle.admin.catalogo.infrastructure.castmember.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, String> {

  Page<CastMemberJpaEntity> findAll(Specification<CastMemberJpaEntity> specification,
      Pageable pageable);

  @Query("SELECT c.id FROM CastMemberJpaEntity c WHERE c.id IN :ids")
  List<String> existsByIds(@Param("ids") List<String> ids);

}
