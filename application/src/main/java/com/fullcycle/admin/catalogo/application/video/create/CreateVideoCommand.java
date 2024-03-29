package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import java.time.Year;
import java.util.Optional;
import java.util.Set;

public record CreateVideoCommand(
    String title,
    String description,
    Year launchedAt,
    double duration,
    Rating rating,
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

  public static CreateVideoCommand with(
      String title,
      String description,
      Year launchedAt,
      double duration,
      Rating rating,
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
    return new CreateVideoCommand(
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


