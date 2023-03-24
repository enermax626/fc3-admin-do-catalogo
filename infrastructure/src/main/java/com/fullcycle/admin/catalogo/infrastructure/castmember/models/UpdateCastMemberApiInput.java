package com.fullcycle.admin.catalogo.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberApiInput(
    @JsonProperty("name") String name,
    @JsonProperty("type") CastMemberType type
) {

}
