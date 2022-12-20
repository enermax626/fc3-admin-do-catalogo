package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
    final var expectedPropertyName = "name";
    final var expectedExceptionMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.name";

    final var aCategory = Category.newCategory("Filmes", "Melhores filmes", true);

    final var anEntity = CategoryJpaEntity.from(aCategory);

    anEntity.setName(null);

    DataIntegrityViolationException dataIntegrityViolationException = Assertions.assertThrows(
        DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

    PropertyValueException actualCause = assertInstanceOf(
        PropertyValueException.class, dataIntegrityViolationException.getCause());

    assertEquals(expectedPropertyName, actualCause.getPropertyName());
    assertEquals(expectedExceptionMessage, actualCause.getMessage());
  }

  @Test
  public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
    final var expectedPropertyName = "createdAt";
    final var expectedExceptionMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

    final var aCategory = Category.newCategory("Filmes", "Melhores filmes", true);

    final var anEntity = CategoryJpaEntity.from(aCategory);

    anEntity.setCreatedAt(null);

    DataIntegrityViolationException dataIntegrityViolationException = Assertions.assertThrows(
        DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

    PropertyValueException actualCause = assertInstanceOf(
        PropertyValueException.class, dataIntegrityViolationException.getCause());

    assertEquals(expectedPropertyName, actualCause.getPropertyName());
    assertEquals(expectedExceptionMessage, actualCause.getMessage());
  }

  @Test
  public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
    final var expectedPropertyName = "updatedAt";
    final var expectedExceptionMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

    final var aCategory = Category.newCategory("Filmes", "Melhores filmes", true);

    final var anEntity = CategoryJpaEntity.from(aCategory);

    anEntity.setUpdatedAt(null);

    DataIntegrityViolationException dataIntegrityViolationException = Assertions.assertThrows(
        DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

    PropertyValueException actualCause = assertInstanceOf(
        PropertyValueException.class, dataIntegrityViolationException.getCause());

    assertEquals(expectedPropertyName, actualCause.getPropertyName());
    assertEquals(expectedExceptionMessage, actualCause.getMessage());
  }
}
