package com.fullcycle.admin.catalogo.application.video.update;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.Video;
import java.util.Set;
import java.util.stream.Collectors;

public record UpdateVideoOutput(
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
    AudioVideoMedia video,
    AudioVideoMedia trailer,
    ImageMedia banner,
    ImageMedia thumbnail,
    ImageMedia thumbnailHalf

) {

  public static UpdateVideoOutput from(Video aVideo) {
    return new UpdateVideoOutput(
        aVideo.getId().getValue(),
        aVideo.getTitle(),
        aVideo.getDescription(),
        aVideo.getLaunchedAt() != null ? aVideo.getLaunchedAt().getValue() : null,
        aVideo.getDuration(),
        aVideo.getRating() != null ? aVideo.getRating().name() : null,
        aVideo.getOpened(),
        aVideo.getPublished(),
        aVideo.getCategories().stream().map(CategoryId::getValue).collect(Collectors.toSet()),
        aVideo.getGenres().stream().map(GenreId::getValue).collect(Collectors.toSet()),
        aVideo.getMembers().stream().map(CastMemberId::getValue).collect(Collectors.toSet()),
        aVideo.getVideo().orElse(null),
        aVideo.getTrailer().orElse(null),
        aVideo.getBanner().orElse(null),
        aVideo.getThumbnail().orElse(null),
        aVideo.getThumbnailHalf().orElse(null)
    );
  }
}
