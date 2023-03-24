package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases;

import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUsecase;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenreUseCaseConfig {

  private final GenreGateway genreGateway;

  private final CategoryGateway categoryGateway;

  public GenreUseCaseConfig(GenreGateway genreGateway, CategoryGateway categoryGateway) {
    this.genreGateway = genreGateway;
    this.categoryGateway = categoryGateway;
  }

  @Bean
  public CreateGenreUseCase createGenreUseCase() {
    return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
  }

  @Bean
  public ListGenreUseCase listGenreUseCase() {
    return new DefaultListGenreUseCase(genreGateway);
  }

  @Bean
  public DeleteGenreUseCase deleteGenreUseCase() {
    return new DefaultDeleteGenreUseCase(genreGateway);
  }

  @Bean
  public GetGenreUseCase getGenreUseCase() {
    return new DefaultGetGenreUseCase(genreGateway);
  }

  @Bean
  public UpdateGenreUsecase updateGenreUsecase() {
    return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
  }

}
