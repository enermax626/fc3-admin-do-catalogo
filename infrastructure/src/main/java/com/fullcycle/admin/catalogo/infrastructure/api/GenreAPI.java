package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreApiInput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RequestMapping("/genre")
@Tag(name = "Genre", description = "Genre API")
public interface GenreAPI {

  @Operation(summary = "Create a new genre")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Genre created"),
      @ApiResponse(responseCode = "422", description = "A validation error has occurred"),
      @ApiResponse(responseCode = "500", description = "Internal server error")})
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<?> create(@RequestBody CreateGenreApiInput request);


  @Operation(summary = "List genres paginated")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listed successfully"),
      @ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  Pagination<GenreListApiOutput> list(
      @RequestParam(name = "search", required = false, defaultValue = "") final String search,
      @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
      @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
      @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
  );


  @Operation(summary = "Get Genre by id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Listed successfully"),
      @ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  @GetMapping(value = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  ResponseEntity<GenreApiOutput> getById(@PathVariable(name = "id") final String id);


  @Operation(summary = "Update a category")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Genre updated successfully"),
      @ApiResponse(responseCode = "404", description = "Genre was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  @PutMapping(path = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<?> updateById(@PathVariable(name = "id") final String id,
      @RequestBody UpdateGenreApiInput request);

  @Operation(summary = "Update a category")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  @DeleteMapping(path = "/{id}")
  void deleteById(@PathVariable(name = "id") final String id);

}
