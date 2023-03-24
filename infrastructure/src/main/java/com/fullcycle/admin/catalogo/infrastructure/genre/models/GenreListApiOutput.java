package com.fullcycle.admin.catalogo.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

public record GenreListApiOutput(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("categories_id") List<String> categories,
    @JsonProperty("is_active") Boolean isActive,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("deleted_at") Instant deletedAt
) {

}
