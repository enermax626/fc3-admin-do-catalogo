package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;

public class DefaultUpdateCastMemberUseCase extends
    UpdateCastMemberUsecase {

  private final CastMemberGateway castMemberGateway;

  public DefaultUpdateCastMemberUseCase(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = castMemberGateway;
  }

  @Override
  public UpdateCastmemberOutput execute(UpdateCastMemberCommand input) {

    CastMember castMember = this.castMemberGateway.findById(CastMemberId.from(input.id()))
        .orElseThrow(() -> NotFoundException.with(CastMember.class, CastMemberId.from(input.id())));

    Notification notification = Notification.create();
    notification.validate(() -> castMember.update(input.name(), input.type()));

    if(notification.hasError()) {
      throw new NotificationException("CastMember with id %s is not valid".formatted(input.id()), notification);
    }

    return UpdateCastmemberOutput.from(this.castMemberGateway.update(castMember));
  }
}
