package com.fullcycle.admin.catalogo.application.category.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateCategoryUseCaseIT {

  @Autowired
  private CreateCategoryUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @SpyBean
  private CategoryGateway categoryGateway;

  @Test
  public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    assertEquals(0, categoryRepository.count());

    final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,
        expectedIsActive);

    final CreateCategoryOutput actualOutput = useCase.execute(aCommand).get();

    assertEquals(1, categoryRepository.count());

    assertNotNull(actualOutput);
    assertNotNull(actualOutput.id());

    final var actualPersistedCategory = categoryRepository.findById(actualOutput.id())
        .get();

    assertEquals(actualOutput.id(), actualPersistedCategory.getId());
    assertEquals(expectedName, actualPersistedCategory.getName());
    assertEquals(expectedDescription, actualPersistedCategory.getDescription());
    assertNotNull(actualPersistedCategory.getCreatedAt());
    assertNotNull(actualPersistedCategory.getUpdatedAt());
    assertNull(actualPersistedCategory.getDeletedAt());
  }

  @Test
  public void givenAnEmptyCommandName_whenCallsCreateCategory_shouldThrowAnError() {
    final var expectedName = "";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,
        expectedIsActive);

    assertEquals(0, categoryRepository.count());

    Notification notification = useCase.execute(aCommand).getLeft();

    assertEquals(0, categoryRepository.count());

    assertEquals(1, notification.getErrors().size());
    assertEquals("'name' should not be empty", notification.getErrors().get(0).message());

    verify(categoryGateway, times(0)).create(any());

  }

  @Test
  public void givenAValidInactiveCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;

    final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,
        expectedIsActive);
    assertEquals(0, categoryRepository.count());

    final var actualOutput = useCase.execute(aCommand).get();

    assertEquals(1, categoryRepository.count());

    assertNotNull(actualOutput);
    assertNotNull(actualOutput.id());

    final var actualPersistedCategory = categoryRepository.findById(actualOutput.id())
        .get();

    assertEquals(actualOutput.id(), actualPersistedCategory.getId());
    assertEquals(expectedName, actualPersistedCategory.getName());
    assertEquals(expectedDescription, actualPersistedCategory.getDescription());
    assertNotNull(actualPersistedCategory.getCreatedAt());
    assertNotNull(actualPersistedCategory.getUpdatedAt());
    assertNotNull(actualPersistedCategory.getDeletedAt());

  }

  @Test
  public void givenAGatewayUnexpectedException_whenCallsCreateCategory_shouldThrowThisError() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;

    final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,
        expectedIsActive);

    doThrow(new IllegalStateException("test")).when(categoryGateway).create(any());

    Notification notification = useCase.execute(aCommand).getLeft();

    assertEquals("test", notification.getErrors().get(0).message());
  }

}
