package com.fullcycle.admin.catalogo.application.category.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

  @Autowired
  private UpdateCategoryUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @SpyBean
  private CategoryGateway categoryGateway;

  @Test
  public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
    final var aCategory = Category.newCategory("Film", "teste", true);

    final var expectedId = aCategory.getId();
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    assertEquals(0, categoryRepository.count());

    categoryRepository.save(CategoryJpaEntity.from(aCategory));

    assertEquals(1, categoryRepository.count());

    final var persistedCategory = categoryRepository.findById(aCategory.getId().getValue())
        .get();

    final var aCommand = UpdateCategoryCommand.with(
        persistedCategory.getId(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );


    final var actualOutput = useCase.execute(aCommand).get();

    assertNotNull(actualOutput);
    assertNotNull(actualOutput.id());

    final var updatedCategory = categoryRepository.findById(actualOutput.id().getValue()).get();

    assertEquals(expectedId.getValue(), updatedCategory.getId());
    assertEquals(expectedName, updatedCategory.getName());
    assertEquals(expectedDescription, updatedCategory.getDescription());
    assertEquals(expectedIsActive, updatedCategory.isActive());
    assertTrue(persistedCategory.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt()));
    assertNull(updatedCategory.getDeletedAt());
  }

  @Test
  public void givenACommandWithInvalidName_whenCallsUpdateCategory_shouldReturnNotificationError() {
    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

    final var expectedName = "Fi ";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    assertEquals(0, categoryRepository.count());

    categoryRepository.save(CategoryJpaEntity.from(aCategory));

    assertEquals(1, categoryRepository.count());

    final var persistedCategory = categoryRepository.findById(aCategory.getId().getValue())
        .get();

    final var aCommand = UpdateCategoryCommand.with(
        persistedCategory.getId(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    final var actualOutput = useCase.execute(aCommand).getLeft();

    assertNotNull(actualOutput);
    assertTrue(actualOutput.hasError());
    assertEquals("'name' must be between 3 and 255 characters",actualOutput.getErrors().get(0).message());
    verify(categoryGateway).findById(eq(aCategory.getId()));
    verify(categoryGateway, times(0)).update(any());
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateCategoryToInactive_shouldReturnCategoryId() {
    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

    final var expectedId = aCategory.getId();
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;


    assertEquals(0, categoryRepository.count());

    categoryRepository.save(CategoryJpaEntity.from(aCategory));

    assertEquals(1, categoryRepository.count());

    final var persistedCategory = categoryRepository.findById(aCategory.getId().getValue())
        .get();

    final var aCommand = UpdateCategoryCommand.with(
        persistedCategory.getId(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    final var actualOutput = useCase.execute(aCommand).get();

    final var updatedCategory = categoryRepository.findById(actualOutput.id().getValue()).get();


    assertEquals(expectedId.getValue(), updatedCategory.getId());
    assertEquals(expectedName, updatedCategory.getName());
    assertEquals(expectedDescription, updatedCategory.getDescription());
    assertEquals(expectedIsActive, updatedCategory.isActive());
    assertTrue(persistedCategory.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt()));
    assertNotNull(updatedCategory.getDeletedAt());
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateCategory_shouldThrowAnCategoryGatewayException() {
    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

    final var expectedId = aCategory.getId();
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedExceptionMessage = "Unexpected exception";

    assertEquals(0, categoryRepository.count());

    categoryRepository.save(CategoryJpaEntity.from(aCategory));

    assertEquals(1, categoryRepository.count());

    final var aCommand = UpdateCategoryCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    doThrow(new IllegalStateException(expectedExceptionMessage))
        .when(categoryGateway).update(any());

    final var actualOutput = useCase.execute(aCommand).getLeft();

    assertNotNull(actualOutput);
    assertTrue(actualOutput.hasError());
    assertEquals(expectedExceptionMessage,actualOutput.getErrors().get(0).message());
    verify(categoryGateway).findById(eq(aCategory.getId()));
    verify(categoryGateway).update(argThat(anUpdatedCategory ->
        anUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt()) &&
            Objects.isNull(anUpdatedCategory.getDeletedAt()) &&
            anUpdatedCategory.getActive()));
  }

  @Test
  public void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldThrowNotFoundException() {
    final var expectedId = "123";
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedExceptionMessage = "Category with Id %s was not found".formatted(expectedId);

    final var aCommand = UpdateCategoryCommand.with(
        expectedId,
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    DomainException domainException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));


    assertNotNull(domainException);
    assertEquals(expectedExceptionMessage, domainException.getErrors().get(0).message());
    verify(categoryGateway).findById(eq(CategoryId.from(expectedId)));
    verify(categoryGateway, times(0)).update(any());
  }

}
