package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import java.time.Instant;
import java.util.Set;

public record VideoApiOutput(
    @JsonProperty("id") String id,
    @JsonProperty("title") String title,
    @JsonProperty("description") String description,
    @JsonProperty("launched_at") Integer launchedAt,
    @JsonProperty("duration") double duration,
    @JsonProperty("opened") boolean opened,
    @JsonProperty("published") boolean published,
    @JsonProperty("rating") Rating rating,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("banner") ImageMediaApiOutput banner,
    @JsonProperty("thumbnail") ImageMediaApiOutput thumbnail,
    @JsonProperty("thumbnail_half") ImageMediaApiOutput thumbnailHalf,
    @JsonProperty("video") AudioVideoMediaApiOutput video,
    @JsonProperty("trailer") AudioVideoMediaApiOutput trailer,
    @JsonProperty("categories_id") Set<String> categories,
    @JsonProperty("genres_id") Set<String> genres,
    @JsonProperty("cast_members_id") Set<String> members
    ) {

}
