package com.fullcycle.admin.catalogo.application.category.retrieve.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.retreive.list.CategoryListOutput;
import com.fullcycle.admin.catalogo.application.category.retreive.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class ListCategoriesUseCaseIT {

  @Autowired
  private ListCategoriesUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @SpyBean
  private CategoryGateway categoryGateway;

  @BeforeEach
  void mockUp(){

    final var categories = List.of(
        Category.newCategory("Filmes", "A categoria mais vista", true),
        Category.newCategory("Series", "Otimas series", true),
        Category.newCategory("Kids", "Conteudo para criancas", true),
        Category.newCategory("Sports", "Jogos de varios esportes", true)
    );
    save(categories);
  }


  @Test
  public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories(){
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var expectedItemsCount = 4;

    final var actualResult = useCase.execute(aQuery);

    assertEquals(expectedItemsCount, actualResult.items().size());
    assertEquals(expectedPage, actualResult.currentPage());
    assertEquals(expectedPerPage, actualResult.perPage());
    assertEquals(expectedItemsCount, actualResult.total());
  }

  @Test
  public void givenAValidQueryWithDESCOrder_whenCallsListCategories_thenShouldReturnCategoriesOrdered(){
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "desc";
    final var expectedTotal = 4;
    final var expectedItemsCount = 4;
    final var expectedFirstCategoryName = "Filmes";
    final var expectedLastCategoryName = "Sports";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);


    final var actualResult = useCase.execute(aQuery);

    assertEquals(expectedItemsCount, actualResult.items().size());
    assertEquals(expectedPage, actualResult.currentPage());
    assertEquals(expectedPerPage, actualResult.perPage());
    assertEquals(expectedTotal, actualResult.total());
    assertEquals(expectedLastCategoryName, actualResult.items().get(0).name());
    assertEquals(expectedFirstCategoryName, actualResult.items().get(actualResult.items().size()-1).name());
  }

  @ParameterizedTest()
  @CsvSource({
      "Fil,0,10,1,1,Filmes",
      "otimas series,0,10,1,1,Series"
  })
  public void givenAValidTerm_whenCallsListCategories_thenShouldReturnCategoriesFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final int expectedTotal,
      final String expectedCategoryName
  ){
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);

    assertEquals(expectedItemsCount, actualResult.items().size());
    assertEquals(expectedPage, actualResult.currentPage());
    assertEquals(expectedPerPage, actualResult.perPage());
    assertEquals(expectedTotal, actualResult.total());
    assertEquals(expectedCategoryName, actualResult.items().get(0).name());
  }

  @Test
  public void givenAValidQuery_whenCallsListCategories_thenShouldReturnEmptyCategories(){
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var categories = List.<Category>of();

    categoryRepository.deleteAll();

    final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
    final var expectedItemsCount = 0;
    final var expectedResult = expectedPagination.map(CategoryListOutput::from);

    final var actualResult = useCase.execute(aQuery);

    assertEquals(expectedItemsCount, actualResult.items().size());
    assertEquals(expectedResult, actualResult);
    assertEquals(expectedPage, actualResult.currentPage());
    assertEquals(expectedPerPage, actualResult.perPage());
    assertEquals(categories.size(), actualResult.total());
  }

  @Test
  public void givenAValidQuery_whenCallsListCategoriesDoesntMatchTerm_thenShouldReturnEmptyCategories(){
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "term not found";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    final var expectedResultSize = 0;
    final var expectedItemsCount = 0;

    final var actualResult = useCase.execute(aQuery);

    assertEquals(expectedItemsCount, actualResult.items().size());
    assertEquals(expectedPage, actualResult.currentPage());
    assertEquals(expectedPerPage, actualResult.perPage());
    assertEquals(expectedResultSize, actualResult.total());
  }

  @Test
  public void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException(){
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedException = "Gateway exception";
    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    doThrow(new IllegalStateException(expectedException))
        .when(categoryGateway).findAll(eq(aQuery));

    final var actualResult = assertThrows(IllegalStateException.class,()-> useCase.execute(aQuery));

    assertEquals(expectedException, actualResult.getMessage());
  }

  private void save(List<Category> categories) {
    List<CategoryJpaEntity> categoryJpaEntities = categories.stream().map(CategoryJpaEntity::from)
        .toList();

    categoryRepository.saveAllAndFlush(categoryJpaEntities);
  }
}
