package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Film", "teste", true);

        final var expectedId = aCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(aCategory.getId())))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        verify(categoryGateway).findById(eq(expectedId));
        verify(categoryGateway).update(argThat(anUpdatedCategory -> {
            return Objects.equals(anUpdatedCategory.getName(), expectedName) &&
                    Objects.equals(anUpdatedCategory.getDescription(), expectedDescription) &&
                    aCategory.getUpdatedAt().isBefore(anUpdatedCategory.getUpdatedAt());
        }));
    }

    @Test
    public void givenACommandWithInvalidName_whenCallsUpdateCategory_shouldReturnNotificationError() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var expectedId = aCategory.getId();
        final var expectedName = "Fi ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(aCategory.getId())))
                .thenReturn(Optional.of(aCategory.clone()));

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

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(aCategory.getId())))
                .thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertEquals(expectedId, actualOutput.id());
        verify(categoryGateway).findById(eq(aCategory.getId()));
        verify(categoryGateway).update(argThat(anUpdatedCategory ->
                anUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt()) &&
                Objects.nonNull(anUpdatedCategory.getDeletedAt()) &&
                !anUpdatedCategory.getActive()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldThrowAnCategoryGatewayException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var expectedId = aCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedExceptionMessage = "Unexpected exception";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(aCategory.getId())))
                .thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedExceptionMessage));

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

        when(categoryGateway.findById(eq(CategoryId.from(expectedId))))
                .thenReturn(Optional.empty());


        DomainException domainException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));


        assertNotNull(domainException);
        assertEquals(expectedExceptionMessage, domainException.getErrors().get(0).message());
        verify(categoryGateway).findById(eq(CategoryId.from(expectedId)));
        verify(categoryGateway, times(0)).update(any());
    }

}
