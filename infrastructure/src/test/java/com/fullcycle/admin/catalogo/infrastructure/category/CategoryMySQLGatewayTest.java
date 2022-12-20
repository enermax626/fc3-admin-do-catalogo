package com.fullcycle.admin.catalogo.infrastructure.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {

  @Autowired
  private CategoryMySQLGateway categoryMySQLGateway;

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void givenAValidCategory_whenCallCreate_shouldReturnCategory() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A melhor categoria";
    final var expectedActive = true;

    Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);

    assertEquals(0, categoryRepository.count());

    Category createdCategory = categoryMySQLGateway.create(aCategory);

    assertEquals(1, categoryRepository.count());

    assertEquals(expectedName, createdCategory.getName());
    assertEquals(expectedDescription, createdCategory.getDescription());
    assertEquals(aCategory.getCreatedAt(), createdCategory.getCreatedAt());
    assertEquals(aCategory.getUpdatedAt(), createdCategory.getUpdatedAt());
    assertNull(createdCategory.getDeletedAt());

    CategoryJpaEntity aCategoryEntity = categoryRepository.getById(aCategory.getId().getValue());

    assertEquals(aCategory.getId().getValue(), aCategoryEntity.getId());
    assertEquals(expectedName, aCategoryEntity.getName());
    assertEquals(expectedDescription, aCategoryEntity.getDescription());
    assertEquals(aCategory.getCreatedAt(), aCategoryEntity.getCreatedAt());
    assertEquals(aCategory.getUpdatedAt(), aCategoryEntity.getUpdatedAt());
    assertNull(aCategoryEntity.getDeletedAt());
  }

  @Test
  void givenAValidCategory_whenCallUpdate_shouldReturnUpdatedCategory()
      throws InterruptedException {
    final var expectedName = "Filmes";
    final var expectedDescription = "A melhor categoria";
    final var expectedActive = true;
    final var expectedUpdatedName = "Updated Filmes";

    Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);

    Category createdCategory = categoryMySQLGateway.create(aCategory);

    Thread.sleep(3000);
    Category categoryToUpdate = createdCategory.clone().update(expectedUpdatedName,
        createdCategory.getDescription(), createdCategory.getActive());

    Category updatedCategory = categoryMySQLGateway.update(categoryToUpdate);

    assertEquals(1, categoryRepository.count());
    assertTrue(updatedCategory.getUpdatedAt().isAfter(createdCategory.getUpdatedAt()));
    assertEquals(expectedUpdatedName, updatedCategory.getName());

  }

  @Test
  public void givenAPrePersistedCategoryAndValidCategoryId_whenCallDelete_shouldDeleteCategory() {
    final var aCategory = Category.newCategory("Filmes", "Filmes description", true);

    assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    assertEquals(1, categoryRepository.count());

    categoryMySQLGateway.deleteById(aCategory.getId());

    assertEquals(0, categoryRepository.count());
  }

  @Test
  public void givenAnInvalidCategoryId_whenCallDelete_shouldDeleteCategory() {
    assertEquals(0, categoryRepository.count());

    categoryMySQLGateway.deleteById(CategoryId.from("invalid"));

    assertEquals(0, categoryRepository.count());
  }

  @Test
  public void givenAnExistentCategoryId_whenCallFindById_shouldReturnCategory() {
    final var aCategory = Category.newCategory("Filmes", "Filmes description", true);

    assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    assertEquals(1, categoryRepository.count());

    Optional<Category> optCategory = categoryMySQLGateway.findById(aCategory.getId());

    assertTrue(optCategory.isPresent());
    final var foundCategory = optCategory.get();
    assertEquals(aCategory.getId(), foundCategory.getId());
    assertEquals(aCategory.getName(), foundCategory.getName());

  }

  @Test
  public void givenAValidCategoryIdNotStored_whenCallFindById_shouldReturnEmpty() {

    assertEquals(0, categoryRepository.count());

    Optional<Category> optCategory = categoryMySQLGateway.findById(CategoryId.unique());

    assertTrue(optCategory.isEmpty());
  }

  @Test
  public void givenAnInvalidCategoryId_whenCallFindById_shouldReturnEmpty() {

    assertEquals(0, categoryRepository.count());

    Optional<Category> optCategory = categoryMySQLGateway.findById(CategoryId.from("invalid"));

    assertTrue(optCategory.isEmpty());
  }

  @Test
  public void givenPrePesistedCategories_whenCallFindAll_shouldReturnPaginated() {
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 3;

    final var filmes = Category.newCategory("Filmes", "Filmes description", true);
    final var series = Category.newCategory("Series", "Series description", true);
    final var documentarios = Category.newCategory("Documentarios", "Documentaries description",
        true);

    assertEquals(0, categoryRepository.count());

//        categoryRepository.saveAndFlush(CategoryJpaEntity.from(filmes));
//        categoryRepository.saveAndFlush(CategoryJpaEntity.from(series));
//        categoryRepository.saveAndFlush(CategoryJpaEntity.from(documentarios));

    categoryRepository.saveAllAndFlush(List.of(CategoryJpaEntity.from(filmes),
        CategoryJpaEntity.from(series),
        CategoryJpaEntity.from(documentarios)));

    assertEquals(3, categoryRepository.count());

    final var query = new CategorySearchQuery(expectedPage,
        expectedPerPage, "", "name", "ASC");

    Pagination<Category> categoriesResult = categoryMySQLGateway.findAll(query);

    assertEquals(expectedPage, categoriesResult.currentPage());
    assertEquals(expectedPerPage, categoriesResult.perPage());
    assertEquals(expectedTotal, categoriesResult.total());
    assertEquals(expectedPerPage, categoriesResult.items().size());
    assertEquals(documentarios.getId(), categoriesResult.items().get(0).getId());
  }

  @Test
  public void givenEmptyCategoriesTable_whenCallFindAll_shouldReturnEmptyPage() {
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 0;

    assertEquals(0, categoryRepository.count());

    final var query = new CategorySearchQuery(expectedPage,
        expectedPerPage, "", "name", "asc");

    Pagination<Category> categoriesResult = categoryMySQLGateway.findAll(query);

    assertEquals(expectedPage, categoriesResult.currentPage());
    assertEquals(expectedPerPage, categoriesResult.perPage());
    assertEquals(expectedTotal, categoriesResult.total());
    assertEquals(0, categoriesResult.items().size());

  }

  @Test
  public void givenFollowPagination_whenCallFindAllWithPage1_shouldReturnPaginated() {
    var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 3;

    final var filmes = Category.newCategory("Filmes", "Filmes description", true);
    final var series = Category.newCategory("Series", "Series description", true);
    final var documentarios = Category.newCategory("Documentarios", "Documentaries description",
        true);

    assertEquals(0, categoryRepository.count());

    categoryRepository.saveAllAndFlush(List.of(CategoryJpaEntity.from(filmes),
        CategoryJpaEntity.from(series),
        CategoryJpaEntity.from(documentarios)));

    assertEquals(3, categoryRepository.count());

    final var query = new CategorySearchQuery(expectedPage,
        expectedPerPage, "", "name", "asc");

    Pagination<Category> categoriesResult = categoryMySQLGateway.findAll(query);

    assertEquals(expectedPage, categoriesResult.currentPage());
    assertEquals(expectedPerPage, categoriesResult.perPage());
    assertEquals(expectedTotal, categoriesResult.total());
    assertEquals(expectedPerPage, categoriesResult.items().size());
    assertEquals(documentarios.getId(), categoriesResult.items().get(0).getId());

    // page 1
    expectedPage = 1;

    final var nextQuery = new CategorySearchQuery(1,
        expectedPerPage, "", "name", "asc");

    Pagination<Category> nextCategoriesResult = categoryMySQLGateway.findAll(nextQuery);

    assertEquals(expectedPage, nextCategoriesResult.currentPage());
    assertEquals(expectedPerPage, nextCategoriesResult.perPage());
    assertEquals(expectedTotal, nextCategoriesResult.total());
    assertEquals(expectedPerPage, nextCategoriesResult.items().size());
    assertEquals(filmes.getId(), nextCategoriesResult.items().get(0).getId());

    // page 1
    expectedPage = 2;

    final var next2Query = new CategorySearchQuery(2,
        expectedPerPage, "", "name", "asc");

    Pagination<Category> next2CategoriesResult = categoryMySQLGateway.findAll(next2Query);

    assertEquals(expectedPage, next2CategoriesResult.currentPage());
    assertEquals(expectedPerPage, next2CategoriesResult.perPage());
    assertEquals(expectedTotal, next2CategoriesResult.total());
    assertEquals(expectedPerPage, next2CategoriesResult.items().size());
    assertEquals(series.getId(), next2CategoriesResult.items().get(0).getId());
  }

  @Test
  public void givenPrePesistedCategoriesAndDocAsTerms_whenCallFindAllAndTermsMatchCategoryName_shouldReturnPaginated() {
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 1;

    final var filmes = Category.newCategory("Filmes", "Filmes description", true);
    final var series = Category.newCategory("Series", "Series description", true);
    final var documentarios = Category.newCategory("Documentarios", "Documentaries description",
        true);

    assertEquals(0, categoryRepository.count());

//        categoryRepository.saveAndFlush(CategoryJpaEntity.from(filmes));
//        categoryRepository.saveAndFlush(CategoryJpaEntity.from(series));
//        categoryRepository.saveAndFlush(CategoryJpaEntity.from(documentarios));

    categoryRepository.saveAllAndFlush(List.of(CategoryJpaEntity.from(filmes),
        CategoryJpaEntity.from(series),
        CategoryJpaEntity.from(documentarios)));

    assertEquals(3, categoryRepository.count());

    final var query = new CategorySearchQuery(expectedPage,
        expectedPerPage, "doc", "name", "asc");

    Pagination<Category> categoriesResult = categoryMySQLGateway.findAll(query);

    assertEquals(expectedPage, categoriesResult.currentPage());
    assertEquals(expectedPerPage, categoriesResult.perPage());
    assertEquals(expectedTotal, categoriesResult.total());
    assertEquals(expectedPerPage, categoriesResult.items().size());
    assertEquals(documentarios.getId(), categoriesResult.items().get(0).getId());
  }

  @Test
  public void givenPrePesistedCategoriesAndMaisAssistidaAsTerms_whenCallFindAllAndTermsMatchCategoryDescription_shouldReturnPaginated() {
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 1;

    final var filmes = Category.newCategory("Filmes", "Filmes a mais assistida", true);
    final var series = Category.newCategory("Series", "Series description", true);
    final var documentarios = Category.newCategory("Documentarios", "Documentaries description",
        true);

    assertEquals(0, categoryRepository.count());

    categoryRepository.saveAllAndFlush(List.of(CategoryJpaEntity.from(filmes),
        CategoryJpaEntity.from(series),
        CategoryJpaEntity.from(documentarios)));

    assertEquals(3, categoryRepository.count());

    final var query = new CategorySearchQuery(expectedPage,
        expectedPerPage, "MAIS ASSISTIDA", "name", "ASC");

    Pagination<Category> categoriesResult = categoryMySQLGateway.findAll(query);

    assertEquals(expectedPage, categoriesResult.currentPage());
    assertEquals(expectedPerPage, categoriesResult.perPage());
    assertEquals(expectedTotal, categoriesResult.total());
    assertEquals(expectedPerPage, categoriesResult.items().size());
    assertEquals(filmes.getId(), categoriesResult.items().get(0).getId());
  }

}