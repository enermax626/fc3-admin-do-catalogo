package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import java.time.Instant;

public record CreateCastMemberOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt,
    Instant updatedAt
) {

  public static CreateCastMemberOutput from(CastMember castMember){
    return new CreateCastMemberOutput(
        castMember.getId().getValue(),
        castMember.getName(),
        castMember.getType(),
        castMember.getCreatedAt(),
        castMember.getUpdatedAt()
    );
  }
}
