package com.fullcycle.admin.catalogo.infrastructure.castmember;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

  private final CastMemberRepository castMemberRepository;

  public CastMemberMySQLGateway(CastMemberRepository castMemberRepository) {
    this.castMemberRepository = castMemberRepository;
  }

  @Override
  public CastMember create(CastMember aCastMember) {
    return save(CastMemberJpaEntity.from(aCastMember));
  }

  private CastMember save(CastMemberJpaEntity anEntity) {
    return this.castMemberRepository.save(anEntity).toAggregate();
  }

  @Override
  public void deleteById(CastMemberId anId) {
    if (this.castMemberRepository.existsById(anId.getValue())) {
      this.castMemberRepository.deleteById(anId.getValue());
    }
  }

  @Override
  public Optional<CastMember> findById(CastMemberId anId) {
    return this.castMemberRepository.findById(anId.getValue()).map(CastMemberJpaEntity::toAggregate);
  }

  @Override
  public CastMember update(CastMember aCastMember) {
    return save(CastMemberJpaEntity.from(aCastMember));
  }

  @Override
  public Pagination<CastMember> findAll(SearchQuery aQuery) {

    PageRequest pageRequest = PageRequest.of(aQuery.page(), aQuery.perPage(),
        Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort()));

    Specification<CastMemberJpaEntity> specification = Specification.where(
        SpecificationUtils.like("name", aQuery.terms()));

    Page<CastMemberJpaEntity> result = this.castMemberRepository.findAll(specification,
        pageRequest);

    return new Pagination<>(result.getNumber(), result.getSize(), result.getTotalElements(),
        result.map(CastMemberJpaEntity::toAggregate).toList());

  }

  @Override
  public Set<CastMemberId> existsByIds(Iterable<CastMemberId> castMembersIds) {
    List<String> ids = StreamSupport.stream(castMembersIds.spliterator(), false)
        .map(CastMemberId::getValue).toList();

    return this.castMemberRepository.existsByIds(ids).stream().map(CastMemberId::from)
        .collect(Collectors.toSet());
  }
}
