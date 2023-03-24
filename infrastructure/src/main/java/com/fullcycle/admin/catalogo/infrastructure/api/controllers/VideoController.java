package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoOutput;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreApiInput;
import com.fullcycle.admin.catalogo.infrastructure.utils.HashingUtils;
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoApiInput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoApiInput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.video.presenters.VideoOutputPresenter;
import java.net.URI;
import java.time.Year;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class VideoController implements VideoAPI {

  private final CreateVideoUseCase createVideoUseCase;
  private final GetVideoUseCase getVideoUseCase;
  private final UpdateVideoUseCase updateVideoUseCase;

  public VideoController(CreateVideoUseCase createVideoUseCase, GetVideoUseCase getVideoUseCase,
      UpdateVideoUseCase updateVideoUseCase) {
    this.createVideoUseCase = createVideoUseCase;
    this.getVideoUseCase = getVideoUseCase;
    this.updateVideoUseCase = updateVideoUseCase;
  }

  @Override
  public ResponseEntity<?> createFull(String title, String description, Integer launchedAt,
      Double duration, Boolean opened, Boolean published, Rating rating, Set<String> categories,
      Set<String> castMembers, Set<String> genres, MultipartFile videoFile,
      MultipartFile trailerFile, MultipartFile bannerFile, MultipartFile thumbFile,
      MultipartFile thumbHalfFile) {

    final var aCmd = CreateVideoCommand.with(title, description, Year.of(launchedAt), duration,
        rating,
        opened, published, categories, genres, castMembers, resourceOf(videoFile),
        resourceOf(trailerFile), resourceOf(bannerFile), resourceOf(thumbFile),
        resourceOf(thumbHalfFile));

    CreateVideoOutput output = createVideoUseCase.execute(aCmd);

    return ResponseEntity.created(URI.create("/video/" + output.id()))
        .body(output);
  }

  @Override
  public ResponseEntity<?> createPartial(CreateVideoApiInput input) {
    final var aCmd = CreateVideoCommand.with(input.title(), input.description(),
        Year.of(input.launchedAt()), input.duration(), input.rating(), input.opened(),
        input.published(), input.categories(), input.genres(), input.castMembers(), null, null,
        null, null, null);

    CreateVideoOutput output = createVideoUseCase.execute(aCmd);
    return ResponseEntity.created(URI.create("/video/" + output.id())).body(output);
  }

  @Override
  public VideoApiOutput getById(String id) {

    GetVideoOutput result = getVideoUseCase.execute(id);

    return VideoOutputPresenter.present(result);
  }

  @Override
  public ResponseEntity<?> updateById(String id, UpdateVideoApiInput request) {

    final var aCommand = UpdateVideoCommand.with(
        id,
        request.title(),
        request.description(),
        request.launchedAt(),
        request.duration(),
        request.rating(),
        request.opened(),
        request.published(),
        request.categories(),
        request.genres(),
        request.castMembers()
    );

    UpdateVideoOutput result = updateVideoUseCase.execute(aCommand);

    return ResponseEntity.ok(VideoOutputPresenter.present(result));
  }

  private Resource resourceOf(MultipartFile mediaFile) {
    if (mediaFile == null) {
      return null;
    }

    try {
      return Resource.with(HashingUtils.checksum(mediaFile.getBytes()), mediaFile.getBytes(),
          mediaFile.getContentType(), mediaFile.getOriginalFilename());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
