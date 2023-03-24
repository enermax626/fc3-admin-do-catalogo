package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberCommand(
    String id,
    String name,
    CastMemberType type
) {

  public static UpdateCastMemberCommand with(String id, String name, CastMemberType type){
    return new UpdateCastMemberCommand(id, name, type);
  }

}
