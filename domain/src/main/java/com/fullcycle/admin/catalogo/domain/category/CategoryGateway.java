package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import java.util.Optional;
import java.util.Set;

public interface CategoryGateway {

    Category create(Category aCategory);

    void deleteById(CategoryId anId);

    Optional<Category> findById(CategoryId anId);

    Category update(Category anCategory);

    Pagination<Category> findAll(SearchQuery aQuery);

    Set<CategoryId> existsByIds(Iterable<CategoryId> categoriesIds);
}
