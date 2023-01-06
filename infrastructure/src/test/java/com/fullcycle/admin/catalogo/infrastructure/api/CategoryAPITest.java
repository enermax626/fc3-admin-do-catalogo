package com.fullcycle.admin.catalogo.infrastructure.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retreive.get.CategoryOutput;
import com.fullcycle.admin.catalogo.application.category.retreive.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.retreive.list.CategoryListOutput;
import com.fullcycle.admin.catalogo.application.category.retreive.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import com.fullcycle.admin.catalogo.infrastructure.api.CategoryAPI;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import io.vavr.API;
import java.util.List;
import java.util.Objects;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CreateCategoryUseCase createCategoryUseCase;

  @MockBean
  private UpdateCategoryUseCase updateCategoryUseCase;

  @MockBean
  private GetCategoryByIdUseCase getCategoryByIdUseCase;

  @MockBean
  private DeleteCategoryUseCase deleteCategoryUseCase;

  @MockBean
  private ListCategoriesUseCase listCategoriesUseCase;

  @Test
  public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId()
      throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var aCommand = new CreateCategoryApiInput(expectedName, expectedDescription,
        expectedIsActive);

    when(createCategoryUseCase.execute(any()))
        .thenReturn(API.Right(CreateCategoryOutput.from("123")));

    final var request = post("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(aCommand));

    this.mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            MockMvcResultMatchers.status().isCreated(),
            MockMvcResultMatchers.header().string("Location", "/categories/123"),
            MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
            MockMvcResultMatchers.jsonPath("$.id", equalTo("123")));

    verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAnNullCommandName_whenCallsCreateCategory_shouldReturnNotificationError()
      throws Exception {
    final String expectedName = null;
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "'name' should not be null";

    final var aCommand = new CreateCategoryApiInput(expectedName, expectedDescription,
        expectedIsActive);

    when(createCategoryUseCase.execute(any()))
        .thenReturn(API.Left(Notification.create(new Error("'name' should not be null"))));

    final var request = post("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(aCommand));

    this.mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            MockMvcResultMatchers.status().isUnprocessableEntity(),
            MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
            MockMvcResultMatchers.jsonPath("$.errors", Matchers.notNullValue()),
            MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
            MockMvcResultMatchers.jsonPath("$.errors[0].message",
                equalTo(expectedErrorMessage))
        );

    verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAnNullCommandName_whenCallsCreateCategory_shouldThrowADomainException()
      throws Exception {
    final String expectedName = null;
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "'name' should not be null";

    final var aCommand = new CreateCategoryApiInput(expectedName, expectedDescription,
        expectedIsActive);

    when(createCategoryUseCase.execute(any()))
        .thenThrow(DomainException.with(new Error("'name' should not be null")));

    final var request = post("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(aCommand));

    this.mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            MockMvcResultMatchers.status().isUnprocessableEntity(),
            MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
            MockMvcResultMatchers.jsonPath("$.errors", Matchers.notNullValue()),
            MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
            MockMvcResultMatchers.jsonPath("$.errors[0].message",
                equalTo(expectedErrorMessage))
        );

    verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAValidId_whenCallsGetCategoryById_shouldReturnACategory()
      throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

    final var expectedId = aCategory.getId();

    when(getCategoryByIdUseCase.execute(any()))
        .thenReturn(CategoryOutput.from(aCategory));

    final var request = get("/categories/{id}", expectedId.getValue());

    this.mvc.perform(request)
        .andDo(print())
        // then
        .andExpectAll(
            MockMvcResultMatchers.status().isOk(),
            MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
            MockMvcResultMatchers.jsonPath("$.id", equalTo(expectedId.getValue())),
            MockMvcResultMatchers.jsonPath("$.name", equalTo(expectedName)),
            MockMvcResultMatchers.jsonPath("$.description", equalTo(expectedDescription)),
            MockMvcResultMatchers.jsonPath("$.created_at",
                equalTo(aCategory.getCreatedAt().toString())),
            MockMvcResultMatchers.jsonPath("$.updated_at",
                equalTo(aCategory.getUpdatedAt().toString())),
            MockMvcResultMatchers.jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt()))
        );

    verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId.getValue()));
  }

  @Test
  public void givenAnInvalidId_whenCallsGetCategoryById_shouldReturnNotFound()
      throws Exception {

    final var expectedId = CategoryId.from("123");
    final var expectedErrorMesage = "Category with ID 123 was not found";

    when(getCategoryByIdUseCase.execute(any()))
        .thenThrow(NotFoundException.with(Category.class, expectedId));

    final var request = get("/categories/{id}", expectedId.getValue());

    this.mvc.perform(request)
        .andDo(print())
        // then
        .andExpectAll(
            MockMvcResultMatchers.status().isNotFound(),
            MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMesage))
        );

    verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId.getValue()));
  }

  @Test
  public void givenAValidId_whenCallsDeleteCategoryById_shouldReturnNoContent()
      throws Exception {

    final var expectedId = "123";

    doNothing().when(deleteCategoryUseCase).execute(expectedId);

    final var request = delete("/categories/{id}", expectedId);

    this.mvc.perform(request)
        .andDo(print())
        // then
        .andExpectAll(
            MockMvcResultMatchers.status().isNoContent()
        );

    verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId()
      throws Exception {
    final var expectedId = "123";
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var aCommand = UpdateCategoryApiInput.from(expectedName, expectedDescription,
        expectedIsActive);

    when(updateCategoryUseCase.execute(any()))
        .thenReturn(API.Right(UpdateCategoryOutput.from("123")));

    final var request = put("/categories/{id}", expectedId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(aCommand));

    this.mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            MockMvcResultMatchers.status().isOk());

    verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAnInvalidId_whenCallsUpdateCategory_shouldReturnDomainExceptionNotFound()
      throws Exception {
    final var expectedId = "not-found";
    final var expectedName = "aFilm";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var aCommand = UpdateCategoryApiInput.from(expectedName, expectedDescription,
        expectedIsActive);

    when(updateCategoryUseCase.execute(any()))
        .thenThrow(NotFoundException.with(Category.class, CategoryId.from(expectedId)));

    final var request = put("/categories/{id}", expectedId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(aCommand));

    this.mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            MockMvcResultMatchers.status().isNotFound(),
            MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
            MockMvcResultMatchers.jsonPath("$.message",
                equalTo("Category with ID not-found was not found")));

    verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAnInvalidName_whenCallsUpdateCategory_shouldReturnDomainException()
      throws Exception {
    final var expectedId = "123";
    final var expectedName = "";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var aCommand = UpdateCategoryApiInput.from(expectedName, expectedDescription,
        expectedIsActive);

    when(updateCategoryUseCase.execute(any()))
        .thenReturn(API.Left(Notification.create(new Error("invalid name"))));

    final var request = put("/categories/{id}", expectedId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(aCommand));

    this.mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            MockMvcResultMatchers.status().isUnprocessableEntity(),
            MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
            MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
            MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo("invalid name")));

    verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name()) &&
            Objects.equals(expectedDescription, cmd.description()) &&
            Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories()
      throws Exception {
    // given
    final var aCategory = Category.newCategory("Movies", "NiceMovies", true);
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "movies";
    final var expectedSort = "description";
    final var expectedDirection = "desc";
    final var expectedItemsCount = 1;
    final var expectedTotal = 1;

    final var expectedItems = List.of(CategoryListOutput.from(aCategory));

    when(listCategoriesUseCase.execute(any()))
        .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

    final var request = get("/categories")
        .accept(MediaType.APPLICATION_JSON)
        .queryParam("page", String.valueOf(expectedPage))
        .queryParam("perPage", String.valueOf(expectedPerPage))
        .queryParam("sort", expectedSort)
        .queryParam("dir", expectedDirection)
        .queryParam("search", expectedTerms);

    this.mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            MockMvcResultMatchers.status().isOk(),
            MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
            MockMvcResultMatchers.jsonPath("$.current_page", equalTo(expectedPage)),
            MockMvcResultMatchers.jsonPath("$.per_page", equalTo(expectedPerPage)),
            MockMvcResultMatchers.jsonPath("$.total", equalTo(expectedItemsCount)),
            MockMvcResultMatchers.jsonPath("$.items", hasSize(expectedItemsCount)),
            MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo(aCategory.getName()))
        );

    verify(listCategoriesUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedPage, cmd.page()) &&
            Objects.equals(expectedDirection, cmd.direction()) &&
            Objects.equals(expectedTerms, cmd.terms())
    ));
  }
}
