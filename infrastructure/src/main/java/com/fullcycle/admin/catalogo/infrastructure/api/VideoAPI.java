package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.GetCastMemberApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreApiInput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoApiInput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoApiInput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoApiOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("video")
@Tag(name = "Video", description = "Video API")
public interface VideoAPI {

  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(summary = "Create a new video with medias")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Category created successfully"),
      @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  public ResponseEntity<?> createFull(
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "description", required = false) String description,
      @RequestParam(name = "launched_at", required = false) Integer launchedAt,
      @RequestParam(name = "duration", required = false) Double duration,
      @RequestParam(name = "opened", required = false) Boolean opened,
      @RequestParam(name = "published", required = false) Boolean published,
      @RequestParam(name = "rating", required = false) Rating rating,
      @RequestParam(name = "categories_id", required = false) Set<String> categories,
      @RequestParam(name = "cast_members_id", required = false) Set<String> castMembers,
      @RequestParam(name = "genres_id", required = false) Set<String> genres,
      @RequestParam(name = "video_file", required = false) MultipartFile videoFile,
      @RequestParam(name = "trailer_file", required = false) MultipartFile trailerFile,
      @RequestParam(name = "banner_file", required = false) MultipartFile bannerFile,
      @RequestParam(name = "thumb_file", required = false) MultipartFile thumbFile,
      @RequestParam(name = "thumb_half_file", required = false) MultipartFile thumbHalfFile
  );

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(summary = "Create a new video without medias")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Category created successfully"),
      @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  public ResponseEntity<?> createPartial(@RequestBody CreateVideoApiInput input);

  @GetMapping(
      value = "/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get a video by Id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "video retrieved"),
      @ApiResponse(responseCode = "404", description = "video was not found"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  VideoApiOutput getById(@PathVariable(name = "id") String id);


  @Operation(summary = "Update a video")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Video updated successfully"),
      @ApiResponse(responseCode = "404", description = "Video was not found"),
      @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
      @ApiResponse(responseCode = "500", description = "An internal server error has occurred")
  })
  @PutMapping(path = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<?> updateById(@RequestParam(name = "id") final String id,
      @RequestBody UpdateVideoApiInput request);
}
