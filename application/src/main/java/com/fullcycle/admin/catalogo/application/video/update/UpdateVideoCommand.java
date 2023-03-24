package com.fullcycle.admin.catalogo.application.video.update;

import com.fullcycle.admin.catalogo.domain.resource.Resource;
import java.util.Optional;
import java.util.Set;

public record UpdateVideoCommand(
    String id,
    String title,
    String description,
    Integer launchedAt,
    double duration,
    String rating,
    boolean opened,
    boolean published,
    Set<String> categories,
    Set<String> genres,
    Set<String> members,
    Resource video,
    Resource trailer,
    Resource banner,
    Resource thumbnail,
    Resource thumbnailHalf
) {

  public static UpdateVideoCommand with(
      String id,
      String title,
      String description,
      Integer launchedAt,
      double duration,
      String rating,
      boolean opened,
      boolean published,
      Set<String> categories,
      Set<String> genres,
      Set<String> members,
      Resource video,
      Resource trailer,
      Resource banner,
      Resource thumbnail,
      Resource thumbnailHalf
  ) {
    return new UpdateVideoCommand(
        id,
        title,
        description,
        launchedAt,
        duration,
        rating,
        opened,
        published,
        categories,
        genres,
        members,
        video,
        trailer,
        banner,
        thumbnail,
        thumbnailHalf
    );
  }

  public static UpdateVideoCommand with(
      String id,
      String title,
      String description,
      Integer launchedAt,
      double duration,
      String rating,
      boolean opened,
      boolean published,
      Set<String> categories,
      Set<String> genres,
      Set<String> members
  ) {
    return new UpdateVideoCommand(
        id,
        title,
        description,
        launchedAt,
        duration,
        rating,
        opened,
        published,
        categories,
        genres,
        members,
        null,
        null,
        null,
        null,
        null
    );
  }

  public Optional<Resource> getVideo() {
    return Optional.ofNullable(video);
  }

  public Optional<Resource> getTrailer() {
    return Optional.ofNullable(trailer);
  }

  public Optional<Resource> getBanner() {
    return Optional.ofNullable(banner);
  }

  public Optional<Resource> getThumbnail() {
    return Optional.ofNullable(thumbnail);
  }

  public Optional<Resource> getThumbnailHalf() {
    return Optional.ofNullable(thumbnailHalf);
  }
}
