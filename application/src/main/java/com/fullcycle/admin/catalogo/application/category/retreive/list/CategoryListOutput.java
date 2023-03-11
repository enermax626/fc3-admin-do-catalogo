package com.fullcycle.admin.catalogo.application.category.retreive.list;

import com.fullcycle.admin.catalogo.domain.category.Category;
import java.time.Instant;

public record CategoryListOutput(
    String id,
    String name,
    String description,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {

  public static CategoryListOutput from(final Category aCategory) {
    return new CategoryListOutput(
        aCategory.getId().getValue(),
        aCategory.getName(),
        aCategory.getDescription(),
        aCategory.getActive(),
        aCategory.getCreatedAt(),
        aCategory.getUpdatedAt(),
        aCategory.getDeletedAt()
    );
  }
}
