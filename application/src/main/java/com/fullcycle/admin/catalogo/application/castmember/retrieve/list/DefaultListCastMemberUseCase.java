package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;

public class DefaultListCastMemberUseCase extends
    ListCastMemberUseCase {

  private final CastMemberGateway castMemberGateway;

  public DefaultListCastMemberUseCase(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = castMemberGateway;
  }

  @Override
  public Pagination<ListCastMemberOutput> execute(SearchQuery input) {
    return this.castMemberGateway.findAll(input)
        .map(ListCastMemberOutput::from);
  }
}
