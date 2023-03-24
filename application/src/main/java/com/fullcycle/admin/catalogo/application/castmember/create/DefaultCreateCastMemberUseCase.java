package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;

public final class DefaultCreateCastMemberUseCase extends
    CreateCastMemberUseCase {

  private final CastMemberGateway castMemberGateway;

  public DefaultCreateCastMemberUseCase(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = castMemberGateway;
  }

  @Override
  public CreateCastMemberOutput execute(CreateCastMemberCommand input) {

    Notification notification = Notification.create();

    final var castMember = notification.validate(
        () -> CastMember.newMember(input.name(), input.type()));

    if (notification.hasError()) {
      throw new NotificationException("CastMember is not valid", notification);
    }

    return CreateCastMemberOutput.from(this.castMemberGateway.create(castMember));
  }
}
