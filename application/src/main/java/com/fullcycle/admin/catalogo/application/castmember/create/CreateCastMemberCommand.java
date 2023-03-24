package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberCommand(
    String name,
    CastMemberType type
) {

  public static CreateCastMemberCommand with(String name, CastMemberType type){
    return new CreateCastMemberCommand(name, type);
  }
}
