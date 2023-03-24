package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;

public class DefaultGetCastMemberUseCase extends
    GetCastMemberUseCase {

  private final CastMemberGateway castMemberGateway;

  public DefaultGetCastMemberUseCase(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = castMemberGateway;
  }

  @Override
  public GetCastMemberOutput execute(String anId) {

    CastMember castMember = this.castMemberGateway.findById(CastMemberId.from(anId))
        .orElseThrow(() -> NotFoundException.with(
            CastMember.class, CastMemberId.from(anId)));

    return GetCastMemberOutput.from(castMember);
  }
}
