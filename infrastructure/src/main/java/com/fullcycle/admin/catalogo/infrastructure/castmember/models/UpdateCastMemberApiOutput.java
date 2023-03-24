package com.fullcycle.admin.catalogo.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastmemberOutput;

public record UpdateCastMemberApiOutput (
    @JsonProperty("id") String id
){

  public static UpdateCastMemberApiOutput from(UpdateCastmemberOutput aUpdateCastmemberOutput) {
    return new UpdateCastMemberApiOutput(
        aUpdateCastmemberOutput.id()
    );
  }

}
