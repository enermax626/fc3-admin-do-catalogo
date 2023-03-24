package com.fullcycle.admin.catalogo.domain.genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;


public class GenreTest {

  @Test
  public void givenValidParams_whenCreateGenre_thenGenreCreated() {
    final var expectedName = "Action";
    final var expectedIsActive = true;

    final var genre = Genre.newGenre(expectedName, expectedIsActive);

    assertNotNull(genre.getId());
    assertEquals(expectedName, genre.getName());
    assertEquals(expectedIsActive, genre.getIsActive());

  }

  @Test
  public void givenNullName_whenCreateGenre_shouldReceiveError() {
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var actualException = assertThrows(DomainException.class,
        () -> Genre.newGenre(expectedName, expectedIsActive));

    assertEquals(expectedErrorCount, actualException.getErrors().size());
    assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAnActiveGenre_whenCallInactivate_shouldReceiveOk() {
    final var expectedName = "Action";

    final var genre = Genre.newGenre(expectedName, true);

    assertTrue(genre.getIsActive());
    assertNull(genre.getDeletedAt());

    genre.deactivate();

    assertFalse(genre.getIsActive());
    assertNotNull(genre.getDeletedAt());
  }

  @Test
  public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOk() throws InterruptedException {
    final var expectedName = "Action";
    final var genre = Genre.newGenre(expectedName, false);

    final var createdAt = genre.getCreatedAt();
    final var updatedAt = genre.getUpdatedAt();

    assertFalse(genre.getIsActive());
    assertNotNull(genre.getDeletedAt());

    Thread.sleep(1000);
    genre.activate();

    assertEquals(createdAt, genre.getCreatedAt());
    assertTrue(updatedAt.isBefore(genre.getUpdatedAt()));

    assertTrue(genre.getIsActive());
    assertNull(genre.getDeletedAt());
  }

  @Test
  public void givenAGenre_whenCallUpdate_shouldGetGenreUpdated() throws InterruptedException {
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategoryId = CategoryId.unique();
    final var expectedCategories = Set.of(expectedCategoryId);

    final var actualGenre = Genre.newGenre(expectedName, false);

    final var createdAt = actualGenre.getCreatedAt();
    final var updatedAt = actualGenre.getUpdatedAt();

    assertFalse(actualGenre.getIsActive());
    assertNotNull(actualGenre.getDeletedAt());

    Thread.sleep(1000);
    Genre updatedGenre = actualGenre.update(expectedName, expectedIsActive, expectedCategories);

    assertNull(updatedGenre.getDeletedAt());
    assertTrue(updatedGenre.getIsActive());
    assertTrue(updatedAt.isBefore(updatedGenre.getUpdatedAt()));
    assertFalse(updatedGenre.getCategories().isEmpty());
  }

  @Test
  public void givenAGenre_whenCallUpdateWithNullCategories_shouldGetGenreUpdated() throws InterruptedException {
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = new ArrayList<>();

    final var actualGenre = Genre.newGenre(expectedName, true);

    final var updatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1000);
    Genre updatedGenre = actualGenre.update(expectedName, expectedIsActive, null);

    assertTrue(updatedAt.isBefore(updatedGenre.getUpdatedAt()));
    assertNotNull(updatedGenre.getCategories());
    assertTrue(updatedGenre.getCategories().isEmpty());
  }

  @Test
  public void givenAGenreWithTwoCategories_whenCallRemoveCategory_shouldRemoveCategory() throws InterruptedException {
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var movieCatId = CategoryId.from("123");
    final var seriesCatId = CategoryId.from("456");
    final var expectedCategories = Set.of(movieCatId);

    final var genre = Genre.newGenre(expectedName, true);

    genre.update(expectedName, expectedIsActive, Set.of(movieCatId, seriesCatId));

    genre.removeCategory(seriesCatId);

    assertFalse(genre.getCategories().isEmpty());
    assertEquals(expectedCategories, genre.getCategories());
  }

  @Test
  public void givenAGenreWithOneCategory_whenCallAddCategory_shouldAddCategoryToGenre() throws InterruptedException {
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var movieCatId = CategoryId.from("123");
    final var seriesCatId = CategoryId.from("456");
    final var expectedCategoriesCount = 2;
    final var expectedCategories = Set.of(movieCatId,seriesCatId);

    final var genre = Genre.newGenre(expectedName, true);

    genre.update(expectedName, expectedIsActive, Set.of(movieCatId));

    genre.addCategory(seriesCatId);

    assertFalse(genre.getCategories().isEmpty());
    assertEquals(expectedCategories, genre.getCategories());
    assertEquals(expectedCategoriesCount, genre.getCategories().size());
  }

  @Test
  public void givenAGenreWithOneCategory_whenCallAddCategoryThreeTimesForSameCategory_shouldAddCategoryToGenreJustOneTime() throws InterruptedException {
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var movieCatId = CategoryId.from("123");
    final var seriesCatId = CategoryId.from("456");
    final var expectedCategoriesCount = 2;
    final var expectedCategories = Set.of(movieCatId,seriesCatId);

    final var genre = Genre.newGenre(expectedName, true);

    genre.update(expectedName, expectedIsActive, Set.of(movieCatId));

    genre.addCategory(seriesCatId);
    genre.addCategory(seriesCatId);
    genre.addCategory(seriesCatId);

    assertFalse(genre.getCategories().isEmpty());
    assertEquals(expectedCategories, genre.getCategories());
    assertEquals(expectedCategoriesCount, genre.getCategories().size());
  }

}
