package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import java.time.Instant;
import java.util.List;

public record GenreOutput(
    String id,
    String name,
    Boolean isActive,
    List<String> categories,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {

  public static GenreOutput from(final Genre genre) {
    return new GenreOutput(
        genre.getId().toString(),
        genre.getName(),
        genre.getIsActive(),
        genre.getCategories().stream().map(CategoryId::toString).toList(),
        genre.getCreatedAt(),
        genre.getUpdatedAt(),
        genre.getDeletedAt()
    );
  }

}
