package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import java.time.Instant;

public record ListCastMemberOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt
) {

  public static ListCastMemberOutput from(CastMember castMember){
    return new ListCastMemberOutput(
        castMember.getId().getValue(),
        castMember.getName(),
        castMember.getType(),
        castMember.getCreatedAt()
    );
  }

}
