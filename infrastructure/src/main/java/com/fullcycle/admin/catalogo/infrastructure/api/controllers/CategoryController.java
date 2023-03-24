package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import static java.util.Objects.isNull;

import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retreive.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.retreive.list.CategoryListOutput;
import com.fullcycle.admin.catalogo.application.category.retreive.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.infrastructure.api.CategoryAPI;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
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

  private final UpdateCategoryUseCase updateCategoryUseCase;

  private final DeleteCategoryUseCase deleteCategoryUseCase;

  private final ListCategoriesUseCase listCategoriesUseCase;

  public CategoryController(CreateCategoryUseCase createCategoryUseCase,
      GetCategoryByIdUseCase getCategoryByIdUseCase, UpdateCategoryUseCase updateCategoryUseCase,
      DeleteCategoryUseCase deleteCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
    this.createCategoryUseCase = createCategoryUseCase;
    this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    this.updateCategoryUseCase = updateCategoryUseCase;
    this.deleteCategoryUseCase = deleteCategoryUseCase;
    this.listCategoriesUseCase = listCategoriesUseCase;
  }

  @Override
  public ResponseEntity<?> updateById(String id, UpdateCategoryApiInput anInput) {

    final var anCommand =
        UpdateCategoryCommand.with(
            id,
            anInput.name(),
            anInput.description(),
            isNull(anInput.active()) ? true : anInput.active());

    Function<Notification, ResponseEntity<?>> onError = notification ->
        ResponseEntity.unprocessableEntity().body(notification);

    Function<UpdateCategoryOutput, ResponseEntity<?>> onSucess = ResponseEntity::ok;

    return updateCategoryUseCase.execute(anCommand).fold(onError, onSucess);
  }

  @Override
  public void deleteById(String id) {
    deleteCategoryUseCase.execute(id);
  }

  @Override
  public ResponseEntity<?> createCategory(@RequestBody CreateCategoryApiInput anInput) {

    final var anCommand =
        CreateCategoryCommand.with(
            anInput.name(),
            anInput.description(),
            isNull(anInput.active()) ? true : anInput.active());

    Function<Notification, ResponseEntity<?>> onError = notification ->
        ResponseEntity.unprocessableEntity().body(notification);

    Function<CreateCategoryOutput, ResponseEntity<?>> onSucess = output ->
        ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

    return createCategoryUseCase.execute(anCommand).fold(onError, onSucess);
  }

  @Override
  public Pagination<CategoryListOutput> listCategories(String search, int page, int perPage,
      String sort, String direction) {

    final var query = SearchQuery.with(page, perPage, search, sort, direction);

    return listCategoriesUseCase.execute(query);
  }

  @Override
  public CategoryApiOutput getById(String categoryId) {
    return CategoryApiPresenter.present(getCategoryByIdUseCase.execute(categoryId));
  }
}
