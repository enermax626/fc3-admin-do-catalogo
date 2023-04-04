package com.fullcycle.admin.catalogo.application.genre.retrieve.list;

import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import java.time.Instant;
import java.util.List;

public record GenreListOutput(
    String id,
    String name,
    Boolean isActive,
    List<String> categories,
    Instant createdAt,
    Instant deletedAt
) {

  public static GenreListOutput from(final Genre genre) {
    return new GenreListOutput(
        genre.getId().getValue(),
        genre.getName(),
        genre.getIsActive(),
        genre.getCategories().stream().map(CategoryId::getValue).toList(),
        genre.getCreatedAt(),
        genre.getDeletedAt()
    );
  }

}
