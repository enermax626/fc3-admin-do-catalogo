package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;

public record AudioVideoMediaApiOutput(
    @JsonProperty("id") String id,
    @JsonProperty("checksum") String checksum,
    @JsonProperty("name") String name,
    @JsonProperty("location") String location,
    @JsonProperty("encoded_location") String encodedLocation,
    @JsonProperty("status") MediaStatus status
) {

}
