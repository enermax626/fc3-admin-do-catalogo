package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import java.util.Optional;
import java.util.Set;

public interface CastMemberGateway {

  CastMember create(CastMember aCastMember);

  void deleteById(CastMemberId anId);

  Optional<CastMember> findById(CastMemberId anId);

  CastMember update(CastMember aCastMember);

  Pagination<CastMember> findAll(SearchQuery aQuery);

  public Set<CastMemberId> existsByIds(Iterable<CastMemberId> categoriesIds);

}
