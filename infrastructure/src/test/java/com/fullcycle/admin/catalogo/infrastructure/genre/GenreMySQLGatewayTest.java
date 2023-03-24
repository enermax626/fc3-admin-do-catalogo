package com.fullcycle.admin.catalogo.infrastructure.genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

  @Autowired
  private CategoryGateway categoryGateway;

  @Autowired
  private GenreGateway genreGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  public void givenAValidGenre_whenCallsCreate_shouldPersistGenre() {
    final var filmes = categoryGateway.create(
        Category.newCategory("Movies", "Movies Category", true));

    final var expectedName = "Action";
    final var expectedIsActive = true;

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

    final var expectedId = aGenre.getId().getValue();

    assertEquals(0, genreRepository.count());

    final var actualGenre = genreGateway.create(aGenre);

    assertEquals(1, genreRepository.count());

    assertEquals(expectedId, actualGenre.getId().getValue());
    assertEquals(expectedName, actualGenre.getName());
    assertEquals(expectedIsActive, actualGenre.getIsActive());
    assertTrue(actualGenre.getCategories().isEmpty());
    assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    assertNull(actualGenre.getDeletedAt());

    final var persistedGenreJpaEntity = genreRepository.findById(expectedId).get();

    assertEquals(expectedName, persistedGenreJpaEntity.getName());
    assertEquals(expectedIsActive, persistedGenreJpaEntity.getIsActive());
    assertTrue(persistedGenreJpaEntity.getCategoriesIds().isEmpty());
    assertEquals(aGenre.getCreatedAt(), persistedGenreJpaEntity.getCreatedAt());
    assertEquals(aGenre.getUpdatedAt(), persistedGenreJpaEntity.getUpdatedAt());
    assertEquals(aGenre.getDeletedAt(), persistedGenreJpaEntity.getDeletedAt());
    assertNull(persistedGenreJpaEntity.getDeletedAt());

  }

  @Test
  public void givenAValidGenreWithCategory_whenCallsCreate_shouldPersistGenre() {
    final var filmes = categoryGateway.create(
        Category.newCategory("Movies", "Movies Category", true));

    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId());

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
    aGenre.addCategory(expectedCategories);

    final var expectedId = aGenre.getId().getValue();

    assertEquals(0, genreRepository.count());

    final var actualGenre = genreGateway.create(aGenre);

    assertEquals(1, genreRepository.count());

    assertEquals(expectedId, actualGenre.getId().getValue());
    assertEquals(expectedName, actualGenre.getName());
    assertEquals(expectedIsActive, actualGenre.getIsActive());
    assertEquals(expectedCategories, actualGenre.getCategories());
    assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    assertNull(actualGenre.getDeletedAt());

    final var persistedGenreJpaEntity = genreRepository.findById(expectedId).get();

    assertEquals(expectedName, persistedGenreJpaEntity.getName());
    assertEquals(expectedIsActive, persistedGenreJpaEntity.getIsActive());
    assertEquals(expectedCategories, persistedGenreJpaEntity.getCategoriesIds());
    assertEquals(aGenre.getCreatedAt(), persistedGenreJpaEntity.getCreatedAt());
    assertEquals(aGenre.getUpdatedAt(), persistedGenreJpaEntity.getUpdatedAt());
    assertEquals(aGenre.getDeletedAt(), persistedGenreJpaEntity.getDeletedAt());
    assertNull(persistedGenreJpaEntity.getDeletedAt());
  }

  @Test
  public void givenAValidGenreWithCategory_whenCallsUpdate_shouldUpdateGenre()
      throws InterruptedException {
    final var filmes = categoryGateway.create(
        Category.newCategory("Movies", "Movies Category", true));

    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId());

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
    aGenre.addCategory(expectedCategories);

    final var expectedId = aGenre.getId().getValue();

    final var actualGenre = genreGateway.create(aGenre);

    assertEquals(expectedId, actualGenre.getId().getValue());
    assertEquals(expectedName, actualGenre.getName());
    assertEquals(expectedIsActive, actualGenre.getIsActive());
    assertEquals(expectedCategories, actualGenre.getCategories());
    assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    assertNull(actualGenre.getDeletedAt());

    actualGenre.update("Action Updated", actualGenre.getIsActive(), actualGenre.getCategories());

    Thread.sleep(150);
    Genre updatedGenre = genreGateway.update(actualGenre);

    assertEquals(actualGenre.getName(), updatedGenre.getName());
    assertEquals(expectedIsActive, updatedGenre.getIsActive());
    assertEquals(expectedCategories, updatedGenre.getCategories());
    assertEquals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt());
    assertTrue(aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt()));
    assertEquals(aGenre.getDeletedAt(), updatedGenre.getDeletedAt());
    assertNull(updatedGenre.getDeletedAt());

    final var persistedGenreJpaEntity = genreRepository.findById(expectedId).get();

    assertEquals(actualGenre.getName(), persistedGenreJpaEntity.getName());
    assertEquals(expectedIsActive, persistedGenreJpaEntity.getIsActive());
    assertEquals(expectedCategories, persistedGenreJpaEntity.getCategoriesIds());
    assertEquals(aGenre.getCreatedAt(), persistedGenreJpaEntity.getCreatedAt());
    assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenreJpaEntity.getUpdatedAt()));
    assertEquals(aGenre.getDeletedAt(), persistedGenreJpaEntity.getDeletedAt());
    assertNull(persistedGenreJpaEntity.getDeletedAt());

  }

  @Test
  public void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
    //given
    final var expectedGenres = List.of();
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 0;
    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery = SearchQuery.with(expectedPage, expectedPerPage, expectedTerms, expectedSort,
        expectedDirection);

    Pagination<Genre> results = this.genreGateway.findAll(aQuery);

    assertEquals(expectedGenres, results.items());
    assertEquals(expectedPage, results.currentPage());
    assertEquals(expectedPerPage, results.perPage());
    assertEquals(expectedTotal, results.total());
  }

  @ParameterizedTest
  @CsvSource({
      "aç, 0, 10, 1, 1, Ação",
      "dr, 0, 10, 1, 1, Drama",
      "com, 0, 10, 1, 1, Comédia Romântica",
  })
  public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
      ) {
    //given
    mockGenres();
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery = SearchQuery.with(expectedPage, expectedPerPage, expectedTerms, expectedSort,
        expectedDirection);

    Pagination<Genre> results = this.genreGateway.findAll(aQuery);

    assertEquals(expectedPage, results.currentPage());
    assertEquals(expectedPerPage, results.perPage());
    assertEquals(expectedItemsCount, results.items().size());
    assertEquals(expectedGenreName, results.items().get(0).getName());
  }

  @ParameterizedTest
  @CsvSource({
      "name, asc, 0, 10, 5, 5, Ação",
      "name, desc, 0, 10, 5, 5, Terror",
      "createdAt, asc, 0, 10, 5, 5, Comédia Romântica",
      "createdAt, desc, 0, 10, 5, 5, Ficção cientifica",
  })
  public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
  ) {
    //given
    mockGenres();

    final var aQuery = SearchQuery.with(expectedPage, expectedPerPage, "", expectedSort,
        expectedDirection);

    Pagination<Genre> results = this.genreGateway.findAll(aQuery);

    assertEquals(expectedPage, results.currentPage());
    assertEquals(expectedPerPage, results.perPage());
    assertEquals(expectedItemsCount, results.items().size());
    assertEquals(expectedGenreName, results.items().get(0).getName());
  }

  @ParameterizedTest
  @CsvSource({
      "0, 2, 2, 5, Ação;Comédia Romântica",
      "1, 2, 2, 5, Drama;Ficção cientifica",
      "2, 2, 1, 5, Terror"
  })
  public void givenAValidPagination_whenCallsFindAll_shouldReturnFiltered(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenres
  ) {
    //given
    mockGenres();

    String expectedSort = "name";
    String expectedDirection = "asc";

    final var aQuery = SearchQuery.with(expectedPage, expectedPerPage, "", expectedSort,
        expectedDirection);

    Pagination<Genre> results = this.genreGateway.findAll(aQuery);

    assertEquals(expectedPage, results.currentPage());
    assertEquals(expectedPerPage, results.perPage());
    assertEquals(expectedItemsCount, results.items().size());

    int index = 0;
    for(String genre : expectedGenres.split(";")) {
      assertEquals(results.items().get(index).getName(), genre);
      index++;
    }

  }

  private void mockGenres() {
    try {
    this.genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Comédia Romântica", true)));
      Thread.sleep(15);
      this.genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Ação", true)));
      Thread.sleep(15);
      this.genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Drama", true)));
      Thread.sleep(15);
      this.genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Terror", true)));
      Thread.sleep(15);
      this.genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Ficção cientifica", true)));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

  }

}
