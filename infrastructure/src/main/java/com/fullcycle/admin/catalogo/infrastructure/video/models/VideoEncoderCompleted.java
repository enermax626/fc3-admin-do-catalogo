package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("COMPLETED")
public record VideoEncoderCompleted(
    @JsonProperty("id") String id,
    @JsonProperty("output_bucket_path") String outputBucket,
    @JsonProperty("video") VideoMetaData video
    ) implements VideoEncoderResult {

  @Override
  public String getStatus() {
    return "COMPLETED";
  }
}
