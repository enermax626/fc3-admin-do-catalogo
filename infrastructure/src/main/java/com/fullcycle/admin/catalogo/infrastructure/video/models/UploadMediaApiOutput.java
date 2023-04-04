package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;

public record UploadMediaApiOutput (
    @JsonProperty("video_id") String videoId,
    @JsonProperty("media_type") VideoMediaType mediaType
){

}
