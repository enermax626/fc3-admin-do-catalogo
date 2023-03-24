package com.fullcycle.admin.catalogo.domain.events;

import java.time.Instant;

public class VideoMidiaCreated implements DomainEvent{

  private final String videoId;
  private final String filePath;

  private final Instant occurredOn;

  public VideoMidiaCreated(String videoId, String filePath) {
    this.videoId = videoId;
    this.filePath = filePath;
    this.occurredOn = Instant.now();
  }

  public String getVideoId() {
    return videoId;
  }

  public String getFilePath() {
    return filePath;
  }

  @Override
  public Instant occurredOn() {
    return this.occurredOn;
  }
}
