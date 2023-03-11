package com.fullcycle.admin.catalogo.application.genre.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class CreateGenreUseCaseTest {

  @InjectMocks
  private DefaultCreateGenreUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private GenreGateway genreGateway;

  @Test
  public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId(){
    //given
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryId>of();

    final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    when(genreGateway.create(any()))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    //when
    final var actualOutput = useCase.execute(aCommand);

    //then
    assertNotNull(actualOutput);
    assertNotNull(actualOutput.id());

    Mockito.verify(genreGateway).create(argThat(aGenre ->
      Objects.equals(expectedName, aGenre.getName())
        && Objects.equals(expectedIsActive, aGenre.getIsActive())
        && Objects.equals(expectedCategories, aGenre.getCategories())
    ));
  }

  @Test
  public void givenAValidCommand_whenCallsCreateGenreWithCategories_shouldReturnGenreId(){
    //given
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryId>of(CategoryId.from("123"), CategoryId.from("456"));

    final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    when(genreGateway.create(any()))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    when(categoryGateway.existsByIds(any()))
        .thenReturn(expectedCategories);


    //when
    final var actualOutput = useCase.execute(aCommand);

    //then
    assertNotNull(actualOutput);
    assertNotNull(actualOutput.id());

    Mockito.verify(genreGateway).create(argThat(aGenre ->
        Objects.equals(expectedName, aGenre.getName())
            && Objects.equals(expectedIsActive, aGenre.getIsActive())
            && Objects.equals(expectedCategories, aGenre.getCategories())
    ));
  }

  private List<String> asString(List<CategoryId> expectedCategories) {
    return expectedCategories.stream().map(CategoryId::getValue).collect(Collectors.toList());
  }

}