package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "status")
@VideoResponseTypes
public sealed interface VideoEncoderResult permits VideoEncoderCompleted, VideoEncoderError {

  String getStatus();
}
