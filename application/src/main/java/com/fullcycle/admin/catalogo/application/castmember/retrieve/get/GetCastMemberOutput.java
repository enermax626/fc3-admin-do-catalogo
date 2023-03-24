package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import java.time.Instant;

public record GetCastMemberOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt,
    Instant updatedAt
) {

  public static GetCastMemberOutput from(CastMember castMember){
    return new GetCastMemberOutput(
        castMember.getId().getValue(),
        castMember.getName(),
        castMember.getType(),
        castMember.getCreatedAt(),
        castMember.getUpdatedAt()
    );
  }

}
