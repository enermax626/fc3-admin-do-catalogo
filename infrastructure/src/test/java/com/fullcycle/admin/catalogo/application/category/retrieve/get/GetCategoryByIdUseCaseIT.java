package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.retreive.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

  @Autowired
  private GetCategoryByIdUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @SpyBean
  private CategoryGateway categoryGateway;

  @Test
  void givenAValidId_whenCallGetById_shouldReturnCategory() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais vista";
    final var isActive = true;
    final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);

    final var expectedId = aCategory.getId();

    assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    assertEquals(1, categoryRepository.count());


    final var actualCategoryOutput = useCase.execute(expectedId.getValue());

    assertNotNull(actualCategoryOutput);
    assertEquals(expectedId, actualCategoryOutput.id());
    assertEquals(expectedName, actualCategoryOutput.name());
    assertEquals(expectedDescription, actualCategoryOutput.description());
    assertEquals(isActive, actualCategoryOutput.isActive());

    verify(categoryGateway).findById(eq(expectedId));
  }

  @Test
  void givenAnInvalidId_whenCallGetById_shouldReturnNotFound() {
    final var expectedId = CategoryId.from("123123");
    final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

    final var notFoundException =
        assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

    assertEquals(expectedErrorMessage, notFoundException.getMessage());

    verify(categoryGateway).findById(eq(expectedId));
  }


  @Test
  void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
    final var expectedErrorMessage = "Category not found";
    final var expectedId = CategoryId.unique();

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(categoryGateway).findById(expectedId);

    final var illegalStateException =
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    assertEquals(expectedErrorMessage, illegalStateException.getMessage());

    verify(categoryGateway).findById(eq(expectedId));
  }

}
