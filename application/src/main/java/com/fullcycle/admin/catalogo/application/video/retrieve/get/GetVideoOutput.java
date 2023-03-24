package com.fullcycle.admin.catalogo.application.video.retrieve.get;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.utils.CollectionsUtils;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import java.time.Instant;
import java.util.Set;

public record GetVideoOutput(
    String id,
    String title,
    String description,
    Integer launchedAt,
    double duration,
    Rating rating,
    Instant createdAt,
    Instant updatedAt,
    boolean opened,
    boolean published,
    Set<String> categories,
    Set<String> genres,
    Set<String> members,
    ImageMedia banner,
    ImageMedia thumbnail,
    ImageMedia thumbnailHalf,
    AudioVideoMedia trailer,
    AudioVideoMedia video
) {
  public static GetVideoOutput from(Video video){
    return new GetVideoOutput(
        video.getId().getValue(),
        video.getTitle(),
        video.getDescription(),
        video.getLaunchedAt().getValue(),
        video.getDuration(),
        video.getRating(),
        video.getCreatedAt(),
        video.getUpdatedAt(),
        video.getOpened(),
        video.getPublished(),
        CollectionsUtils.mapTo(video.getCategories(), CategoryId::getValue),
        CollectionsUtils.mapTo(video.getGenres(), GenreId::getValue),
        CollectionsUtils.mapTo(video.getMembers(), CastMemberId::getValue),
        video.getBanner().orElse(null),
        video.getThumbnail().orElse(null),
        video.getThumbnailHalf().orElse(null),
        video.getTrailer().orElse(null),
        video.getVideo().orElse(null)
    );
  }

}
