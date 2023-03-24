package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class GetGenreUseCaseIT {


  @Autowired
  private GetGenreUseCase useCase;
  @SpyBean
  private CategoryGateway categoryGateway;
  @SpyBean
  private GenreGateway genreGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  public void givenAValidGenreId_whenCallGetGenre_shouldReturnGenre(){
    //given
    Category category = this.categoryGateway.create(
        Category.newCategory("Filmes", "Filmes category", true));
    final var expectedName = "Filmes";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(category.getId().getValue());

    Genre aGenre = Genre.newGenre("Filmes", true);
    aGenre.addCategory(category.getId());
    this.genreGateway.create(aGenre);
    //when

    GenreOutput genreResult = this.useCase.execute(aGenre.getId().getValue());

    //then

    assertNotNull(genreResult);
    Assertions.assertEquals(expectedName, genreResult.name());
    Assertions.assertEquals(expectedIsActive, genreResult.isActive());
    Assertions.assertEquals(expectedCategories, genreResult.categories());
  }


}
