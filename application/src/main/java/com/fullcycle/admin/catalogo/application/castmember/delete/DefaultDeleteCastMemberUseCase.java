package com.fullcycle.admin.catalogo.application.castmember.delete;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;

public final class DefaultDeleteCastMemberUseCase extends
    DeleteCastMemberUseCase {

  private final CastMemberGateway castMemberGateway;

  public DefaultDeleteCastMemberUseCase(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = castMemberGateway;
  }

  @Override
  public void execute(String input) {
    this.castMemberGateway.deleteById(CastMemberId.from(input));
  }
}
