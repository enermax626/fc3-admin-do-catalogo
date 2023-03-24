package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUsecase;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.api.GenreAPI;
import com.fullcycle.admin.catalogo.infrastructure.category.presenters.GenreApiPresenter;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreApiInput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreApiInput;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenreController implements GenreAPI {

  private final CreateGenreUseCase createGenreUseCase;
  private final GetGenreUseCase getGenreUseCase;
  private final UpdateGenreUsecase updateGenreUsecase;
  private final DeleteGenreUseCase deleteGenreUseCase;
  private final ListGenreUseCase listGenreUseCase;

  public GenreController(CreateGenreUseCase createGenreUseCase, GetGenreUseCase getGenreUseCase,
      UpdateGenreUsecase updateGenreUsecase, DeleteGenreUseCase deleteGenreUseCase,
      ListGenreUseCase listGenreUseCase) {
    this.createGenreUseCase = createGenreUseCase;
    this.getGenreUseCase = getGenreUseCase;
    this.updateGenreUsecase = updateGenreUsecase;
    this.deleteGenreUseCase = deleteGenreUseCase;
    this.listGenreUseCase = listGenreUseCase;
  }

  @Override
  public ResponseEntity<?> create(CreateGenreApiInput request) {
    CreateGenreCommand aCommand = CreateGenreCommand.with(request.name(), request.isActive(),
        request.categories());
    CreateGenreOutput result = this.createGenreUseCase.execute(aCommand);

    return ResponseEntity.created(URI.create("/genres/" + result.id()))
        .body(result);

  }

  @Override
  public Pagination<GenreListApiOutput> list(String search, int page, int perPage,
      String sort, String direction) {

    SearchQuery aQuery = SearchQuery.with(page, perPage, search, sort, direction);

    Pagination<GenreListOutput> result = this.listGenreUseCase.execute(aQuery);

    return result.map(GenreApiPresenter::presentList);
  }

  @Override
  public ResponseEntity<GenreApiOutput> getById(String id) {
    GenreOutput result = this.getGenreUseCase.execute(id);

    return ResponseEntity.ok(GenreApiPresenter.present(result));
  }

  @Override
  public ResponseEntity<?> updateById(String id, UpdateGenreApiInput request) {
    UpdateGenreCommand aCommand = UpdateGenreCommand.with(id, request.name(), request.isActive(),
        request.categories());

    UpdateGenreOutput result = this.updateGenreUsecase.execute(aCommand);

    return ResponseEntity.ok(result);
  }

  @Override
  public void deleteById(String id) {
    this.deleteGenreUseCase.execute(id);
  }
}

