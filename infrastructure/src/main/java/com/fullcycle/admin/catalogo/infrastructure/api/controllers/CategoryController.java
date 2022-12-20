package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retreive.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.infrastructure.api.CategoryAPI;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.fullcycle.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "categories")
public class CategoryController implements CategoryAPI {

  private final CreateCategoryUseCase createCategoryUseCase;

  private final GetCategoryByIdUseCase getCategoryByIdUseCase;

  public CategoryController(CreateCategoryUseCase createCategoryUseCase,
      GetCategoryByIdUseCase getCategoryByIdUseCase) {
    this.createCategoryUseCase = createCategoryUseCase;
    this.getCategoryByIdUseCase = getCategoryByIdUseCase;
  }

  @Override
  public ResponseEntity<?> createCategory(@RequestBody CreateCategoryApiInput anInput) {

    final var anCommand =
        CreateCategoryCommand.with(
            anInput.name(),
            anInput.description(),
            Objects.isNull(anInput.active()) ? true : anInput.active());

    Function<Notification, ResponseEntity<?>> onError = notification ->
        ResponseEntity.unprocessableEntity().body(notification);

    Function<CreateCategoryOutput, ResponseEntity<?>> onSucess = output ->
        ResponseEntity.created(URI.create("/categories/"+output.id())).body(output);

   return createCategoryUseCase.execute(anCommand).fold(onError,onSucess);
  }

  @Override
  public Pagination<?> listCategories(String search, int page, int perPage,
      String sort, String direction) {
    return null;
  }

  @Override
  public CategoryApiOutput getById(String categoryId) {
    return CategoryApiPresenter.present(getCategoryByIdUseCase.execute(categoryId));
  }
}
