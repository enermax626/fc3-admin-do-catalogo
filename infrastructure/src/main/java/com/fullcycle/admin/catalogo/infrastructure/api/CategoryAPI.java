package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "category")
public interface CategoryAPI {

  @PutMapping(path = "{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update a category")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category updated successfully"),
      @ApiResponse(responseCode = "404", description = "Category was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateCategoryApiInput anInput);

  @DeleteMapping(path = "{id}")
  @Operation(summary = "Update a category")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  void deleteById(@PathVariable(name = "id") String id);

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create a new category")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Category created successfully"),
      @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  ResponseEntity<?> createCategory(@RequestBody CreateCategoryApiInput anInput);

  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(summary = "List categories paginated")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listed successfully"),
      @ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  Pagination<?> listCategories(
      @RequestParam(name = "search", required = false, defaultValue = "") final String search,
      @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
      @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
      @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
  );

  @GetMapping(
      value = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a category by Id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category retrieved"),
      @ApiResponse(responseCode = "404", description = "Category was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  CategoryApiOutput getById(@PathVariable(name = "id") String id);

}
