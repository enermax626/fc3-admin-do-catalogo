package com.fullcycle.admin.catalogo.application.genre.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateGenreUseCaseIT {

  @Autowired
  private CreateGenreUseCase useCase;
  @SpyBean
  private CategoryGateway categoryGateway;
  @SpyBean
  private GenreGateway genreGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId(){
    //given
    final var  filmesCategory = Category.newCategory("Filmes", "Filmes category", true);
    Category category = this.categoryGateway.create(filmesCategory);
    final var expectedName = "Filmes";
    final var expectedIsActive = true;
    final var expectedCategories = Set.of(category.getId());

    final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    //when
    final var actualOutput = useCase.execute(aCommand);

    //then
    assertNotNull(actualOutput);
    assertNotNull(actualOutput.id());

    GenreJpaEntity persistedGenre = this.genreRepository.findById(actualOutput.id()).get();
    assertEquals(expectedIsActive, persistedGenre.getIsActive());
    assertTrue(
        expectedCategories.size() == persistedGenre.getCategoriesIds().size()
            && expectedCategories.containsAll(persistedGenre.getCategoriesIds())
    );
    assertEquals(expectedName, persistedGenre.getName());
  }

  private Set<String> asString(Set<CategoryId> expectedCategories) {
    return expectedCategories.stream().map(CategoryId::getValue).collect(Collectors.toSet());
  }
}
