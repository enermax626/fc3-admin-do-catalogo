package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from (final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from (final String aCategoryId) {
        return new CreateCategoryOutput(aCategoryId);
    }
}
