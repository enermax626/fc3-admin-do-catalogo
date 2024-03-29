package com.fullcycle.admin.catalogo.application.category.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

  @Autowired
  private DeleteCategoryUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @SpyBean
  private CategoryGateway categoryGateway;


  @Test
  void givenAValidCommand_whenCallDeleteCategory_shouldDelete() {
    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

    final var categoryId = aCategory.getId();


    assertEquals(0, categoryRepository.count());

    categoryRepository.save(CategoryJpaEntity.from(aCategory));

    assertEquals(1, categoryRepository.count());

    Assertions.assertDoesNotThrow(() -> useCase.execute(categoryId.getValue()));

    assertEquals(0, categoryRepository.count());

  }

  @Test
  void givenAnInvalidId_whenCallDeleteCategory_shouldBeOk() {
    final var anId = CategoryId.from("123123");

    assertEquals(0, categoryRepository.count());

    Assertions.assertDoesNotThrow(() -> useCase.execute(anId.getValue()));

    assertEquals(0, categoryRepository.count());

    verify(categoryGateway).deleteById(eq(anId));

  }

  @Test
  void givenAValidId_whenGatewayThrowsError_shouldReturnException() {
    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

    final var categoryId = aCategory.getId();
    final var expectedException = "Unexpected exception";

    doThrow(new IllegalStateException(expectedException))
        .when(categoryGateway).deleteById(categoryId);

    assertThrows(IllegalStateException.class, () -> useCase.execute(categoryId.getValue()));

    verify(categoryGateway).deleteById(eq(categoryId));
  }


}
