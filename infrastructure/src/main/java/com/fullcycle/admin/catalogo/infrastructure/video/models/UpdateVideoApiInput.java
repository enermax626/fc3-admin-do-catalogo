package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import java.util.Set;

public record UpdateVideoApiInput(
    @JsonProperty("title") String title,
    @JsonProperty("description") String description,
    @JsonProperty("duration") double duration,
    @JsonProperty("launched_at") Integer launchedAt,
    @JsonProperty("opened") Boolean opened,
    @JsonProperty("published") Boolean published,
    @JsonProperty("rating") String rating,
    @JsonProperty("cast_members") Set<String> castMembers,
    @JsonProperty("categories") Set<String> categories,
    @JsonProperty("genres") Set<String> genres
) {

}
