package com.fullcycle.admin.catalogo.infrastructure.category;

import static com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

  private final CategoryRepository categoryRepository;

  public CategoryMySQLGateway(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Category create(Category aCategory) {
    return save(aCategory);
  }


  @Override
  public void deleteById(CategoryId anId) {
      if (categoryRepository.existsById(anId.getValue())) {
          categoryRepository.deleteById(anId.getValue());
      }
  }

  @Override
  public Optional<Category> findById(CategoryId anId) {
    return categoryRepository.findById(anId.getValue()).map(CategoryJpaEntity::toAggregate);
  }

  @Override
  public Category update(Category aCategory) {
    return save(aCategory);
  }

  @Override
  public Pagination<Category> findAll(CategorySearchQuery aQuery) {

    final var pageRequest = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    final var specification = Optional.ofNullable(aQuery.terms())
        .filter(s -> !s.isBlank())
        .map(terms -> SpecificationUtils.<CategoryJpaEntity>like("name", terms)
            .or(like("description", terms)))
        .orElse(null);

    final var pageResult = categoryRepository.findAll(Specification.where(specification),
        pageRequest);

    return new Pagination<>(pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(CategoryJpaEntity::toAggregate).toList());
  }

  private Category save(Category aCategory) {
    return categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
  }
}
