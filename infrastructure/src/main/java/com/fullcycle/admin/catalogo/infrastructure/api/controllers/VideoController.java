package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import static com.fullcycle.admin.catalogo.domain.utils.CollectionsUtils.mapTo;

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaCommand;
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaOutput;
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoOutput;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideoOutput;
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import com.fullcycle.admin.catalogo.domain.video.VideoResource;
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalogo.infrastructure.utils.HashingUtils;
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoApiInput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoApiInput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UploadMediaApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoListApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.video.presenters.VideoOutputPresenter;
import com.google.common.net.HttpHeaders;
import java.net.URI;
import java.time.Year;
import java.util.Set;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class VideoController implements VideoAPI {

  private final CreateVideoUseCase createVideoUseCase;
  private final GetVideoUseCase getVideoUseCase;
  private final UpdateVideoUseCase updateVideoUseCase;
  private final DeleteVideoUseCase deleteVideoUseCase;
  private final ListVideoUseCase listVideoUseCase;
  private final GetMediaUseCase getMediaUseCase;
  private final UploadMediaUseCase uploadMediaUseCase;

  public VideoController(CreateVideoUseCase createVideoUseCase, GetVideoUseCase getVideoUseCase,
      UpdateVideoUseCase updateVideoUseCase, DeleteVideoUseCase deleteVideoUseCase,
      ListVideoUseCase listVideoUseCase, GetMediaUseCase getMediaUseCase,
      UploadMediaUseCase uploadMediaUseCase) {
    this.createVideoUseCase = createVideoUseCase;
    this.getVideoUseCase = getVideoUseCase;
    this.updateVideoUseCase = updateVideoUseCase;
    this.deleteVideoUseCase = deleteVideoUseCase;
    this.listVideoUseCase = listVideoUseCase;
    this.getMediaUseCase = getMediaUseCase;
    this.uploadMediaUseCase = uploadMediaUseCase;
  }

  @Override
  public ResponseEntity<?> createFull(String title, String description, Integer launchedAt,
      Double duration, Boolean opened, Boolean published, Rating rating, Set<String> categories,
      Set<String> castMembers, Set<String> genres, MultipartFile videoFile,
      MultipartFile trailerFile, MultipartFile bannerFile, MultipartFile thumbFile,
      MultipartFile thumbHalfFile) {

    final var aCmd = CreateVideoCommand.with(title, description, Year.of(launchedAt), duration,
        rating, opened, published, categories, genres, castMembers, resourceOf(videoFile),
        resourceOf(trailerFile), resourceOf(bannerFile), resourceOf(thumbFile),
        resourceOf(thumbHalfFile));

    CreateVideoOutput output = createVideoUseCase.execute(aCmd);

    return ResponseEntity.created(URI.create("/video/" + output.id())).body(output);
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

    final var aCommand = UpdateVideoCommand.with(id, request.title(), request.description(),
        request.launchedAt(), request.duration(), request.rating(), request.opened(),
        request.published(), request.categories(), request.genres(), request.castMembers());

    UpdateVideoOutput result = updateVideoUseCase.execute(aCommand);

    return ResponseEntity.ok(VideoOutputPresenter.present(result));
  }

  @Override
  public void deleteById(String id) {
    this.deleteVideoUseCase.execute(id);
  }

  @Override
  public Pagination<VideoListApiOutput> list(String search, int page, int perPage, String sort,
      String direction, Set<String> castMembers, Set<String> categories, Set<String> genres) {
    VideoSearchQuery aQuery = VideoSearchQuery.with(page, perPage, search, sort, direction,
        mapTo(castMembers, CastMemberId::from), mapTo(categories, CategoryId::from),
        mapTo(genres, GenreId::from));

    Pagination<ListVideoOutput> execute = this.listVideoUseCase.execute(aQuery);

    return VideoOutputPresenter.present(execute);
  }

  @Override
  public ResponseEntity<byte[]> getMediaByType(String id, VideoMediaType type) {
    GetMediaCommand aCmd = GetMediaCommand.with(id, type);

    GetMediaOutput result = getMediaUseCase.execute(aCmd);

    return ResponseEntity.ok().contentType(MediaType.valueOf(result.contentType()))
        .contentLength(result.content().length)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(result.name()))
        .body(result.content());
  }

  @Override
  public ResponseEntity<?> uploadMediaByType(String videoId, VideoMediaType type, MultipartFile file) {
    UploadMediaCommand aCmd = UploadMediaCommand.with(videoId,
        VideoResource.with(resourceOf(file), type));

    UploadMediaOutput result = uploadMediaUseCase.execute(aCmd);
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
