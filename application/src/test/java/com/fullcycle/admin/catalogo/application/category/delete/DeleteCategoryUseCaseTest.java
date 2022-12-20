package com.fullcycle.admin.catalogo.application.category.delete;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidCommand_whenCallDeleteCategory_shouldDelete() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var categoryId = aCategory.getId();

        doNothing().when(categoryGateway).deleteById(eq(categoryId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(categoryId.getValue()));

        verify(categoryGateway).deleteById(eq(categoryId));
    }

    @Test
    void givenAnInvalidId_whenCallDeleteCategory_shouldBeOk() {
        final var anId = CategoryId.from("123123");

        doNothing().when(categoryGateway).deleteById(eq(anId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(anId.getValue()));

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
