package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import com.fullcycle.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retreive.get.DefaultGetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallGetById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais vista";
        final var isActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);

        final var expectedId = aCategory.getId();

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(aCategory.clone()));

        final var actualCategoryOutput = useCase.execute(expectedId.getValue());

        assertNotNull(actualCategoryOutput);
        assertEquals(expectedId, actualCategoryOutput.id());
        assertEquals(expectedName, actualCategoryOutput.name());
        assertEquals(expectedDescription, actualCategoryOutput.description());
        assertEquals(isActive, actualCategoryOutput.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategoryOutput.createdAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategoryOutput.updatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategoryOutput.deletedAt());

        verify(categoryGateway).findById(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallGetById_shouldReturnNotFound() {
        final var expectedId = CategoryId.from("123123");
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());


        when(categoryGateway.findById(expectedId)).thenReturn(Optional.empty());

        final var domainException =
                assertThrows(DomainException.class, () -> useCase.execute(expectedId.getValue()));


        assertEquals(expectedErrorMessage, domainException.getErrors().get(0).message());

        verify(categoryGateway).findById(eq(expectedId));
    }


    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Category not found";
        final var expectedId = CategoryId.unique();

        when(categoryGateway.findById(expectedId)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var illegalStateException =
                assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));


        assertEquals(expectedErrorMessage, illegalStateException.getMessage());

        verify(categoryGateway).findById(eq(expectedId));
    }
}