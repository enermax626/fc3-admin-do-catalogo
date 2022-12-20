package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var categoryGateway = Mockito.mock(CategoryGateway.class);
        when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());
        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway).create(argThat(aCategory -> Objects.equals(aCategory.getName(), expectedName)));
    }

    @Test
    public void givenAnEmptyCommandName_whenCallsCreateCategory_shouldThrowAnError() {
        final var expectedName = "";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Notification notification = useCase.execute(aCommand).getLeft();

        assertEquals(1, notification.getErrors().size());
        assertEquals("'name' should not be empty", notification.getErrors().get(0).message());

        verify(categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidInactiveCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway).create(argThat(aCategory -> Objects.equals(aCategory.getName(), expectedName) &&
                Objects.equals(aCategory.getActive(), false) &&
                Objects.nonNull(aCategory.getDeletedAt())));
    }

    @Test
    public void givenAGatewayUnexpectedException_whenCallsCreateCategory_shouldThrowThisError() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        doThrow(new RuntimeException("test")).when(categoryGateway).create(any());

        Notification notification = useCase.execute(aCommand).getLeft();

        assertEquals("test", notification.getErrors().get(0).message());

        verify(categoryGateway).create(argThat(aCategory -> Objects.equals(aCategory.getName(), expectedName) &&
                Objects.equals(aCategory.getActive(), false) &&
                Objects.nonNull(aCategory.getDeletedAt())));
    }
}
