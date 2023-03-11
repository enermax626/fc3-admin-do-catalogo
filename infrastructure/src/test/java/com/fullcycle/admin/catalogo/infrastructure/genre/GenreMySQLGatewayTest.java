package com.fullcycle.admin.catalogo.infrastructure.genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

  @Autowired
  private CategoryGateway categoryGateway;

  @Autowired
  private GenreMySQLGateway genreMySQLGateway;

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

    final var actualGenre = genreMySQLGateway.create(aGenre);

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
    assertEquals(expectedIsActive, persistedGenreJpaEntity.isActive());
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

    final var actualGenre = genreMySQLGateway.create(aGenre);

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
    assertEquals(expectedIsActive, persistedGenreJpaEntity.isActive());
    assertEquals(expectedCategories, persistedGenreJpaEntity.getCategoriesIds());
    assertEquals(aGenre.getCreatedAt(), persistedGenreJpaEntity.getCreatedAt());
    assertEquals(aGenre.getUpdatedAt(), persistedGenreJpaEntity.getUpdatedAt());
    assertEquals(aGenre.getDeletedAt(), persistedGenreJpaEntity.getDeletedAt());
    assertNull(persistedGenreJpaEntity.getDeletedAt());
  }
}
