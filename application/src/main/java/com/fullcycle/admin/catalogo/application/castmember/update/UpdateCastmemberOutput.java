package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;

public record UpdateCastmemberOutput(
    String id
) {

  public static UpdateCastmemberOutput from(CastMember castMember){
    return new UpdateCastmemberOutput(castMember.getId().getValue());
  }

}
