package com.fullcycle.admin.catalogo.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import java.time.Instant;

public record CreateCastMemberApiOutput(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("type") CastMemberType type,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt
) {

    public static CreateCastMemberApiOutput from(CreateCastMemberOutput aCreateCastMemberOutput) {
      return new CreateCastMemberApiOutput(
          aCreateCastMemberOutput.id(),
          aCreateCastMemberOutput.name(),
          aCreateCastMemberOutput.type(),
          aCreateCastMemberOutput.createdAt(),
          aCreateCastMemberOutput.updatedAt()
      );
    }
}
