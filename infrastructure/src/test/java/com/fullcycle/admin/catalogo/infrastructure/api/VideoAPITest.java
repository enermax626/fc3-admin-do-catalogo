package com.fullcycle.admin.catalogo.infrastructure.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoOutput;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.VideoId;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ControllerTest(controllers = VideoAPI.class)
class VideoAPITest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private CreateVideoUseCase createVideoUseCase;
  @MockBean
  private GetVideoUseCase getVideoUseCase;
  @MockBean
  private UpdateVideoUseCase updateVideoUseCase;
  @MockBean
  private DeleteVideoUseCase deleteVideoUseCase;
  @MockBean
  private ListVideoUseCase listVideoUseCase;
  @MockBean
  private GetMediaUseCase getMediaUseCase;
  @MockBean
  private UploadMediaUseCase uploadMediaUseCase;

  @Test
  public void givenAValidCommand_whenCallCreateFull_shouldReturnAnId() throws Exception {
    // given
    final var expectedCastMember = CastMember.newMember("Wesley", CastMemberType.ACTOR);
    final var expectedCategory = Category.newCategory("Terror", "Descrição do terror", true);
    final var expectedGenre = Genre.newGenre("Suspense", true);

    final var expectedId = VideoId.unique();
    final var expectedTitle = "Olhos famintos";
    final var expectedDescription = "Um filme de terror";
    final var expectedLaunchedAt = Year.of(1997);
    final var expectedDuration = 120;
    final var expectedOpened = true;
    final var expectedPublished = true;
    final var expectedRating = Rating.AGE_16;
    final var expectedCategories = Set.of(expectedCategory.getId().getValue());
    final var expectedGenres = Set.of(expectedGenre.getId().getValue());
    final var expectedCastMembers = Set.of(expectedCastMember.getId().getValue());
    final var expectedVideoMock = new MockMultipartFile("video_file", "video.mp4", "video/mp4",
        "VIDEO_CONTENT_HERE".getBytes());
    final var expectedTrailerMock = new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER_CONTENT_HERE".getBytes());
    final var expectedBannerMock = new MockMultipartFile("banner_file", "banner.jpg", "image/jpeg", "BANNER_CONTENT_HERE".getBytes());
    final var expectedThumbnailMock = new MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpeg", "THUMBNAIL_CONTENT_HERE".getBytes());
    final var expectedThumbnailHalfMock = new MockMultipartFile("thumb_half_file", "thumbnailHalf.jpg", "image/jpeg", "THUMBNAIL_HALF_CONTENT_HERE".getBytes());

    when(createVideoUseCase.execute(any())).thenReturn(CreateVideoOutput.create(expectedId.getValue()));

    // when

    final var aRequest = MockMvcRequestBuilders.multipart("/video")
        .file(expectedVideoMock)
        .file(expectedTrailerMock)
        .file(expectedBannerMock)
        .file(expectedThumbnailMock)
        .file(expectedThumbnailHalfMock)
        .param("title", expectedTitle)
        .param("description", expectedDescription)
        .param("launched_at", expectedLaunchedAt.toString())
        .param("duration", String.valueOf(expectedDuration))
        .param("opened", String.valueOf(expectedOpened))
        .param("published", String.valueOf(expectedPublished))
        .param("rating", expectedRating.toString())
        .param("categories_id", String.join(",", expectedCategories))
        .param("genres_id", String.join(",", expectedGenres))
        .param("cast_members_id", String.join(",", expectedCastMembers))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.MULTIPART_FORM_DATA);

    ResultActions result = this.mockMvc.perform(aRequest)
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(header().string("Location", "/video/" + expectedId.getValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(expectedId.getValue()));

    ArgumentCaptor<CreateVideoCommand> commandCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

    verify(createVideoUseCase, times(1)).execute(commandCaptor.capture());

    CreateVideoCommand actualCmd = commandCaptor.getValue();
    assertEquals(expectedTitle, actualCmd.title());
    assertEquals(expectedDescription, actualCmd.description());
    assertEquals(expectedLaunchedAt, actualCmd.launchedAt());
    assertEquals(expectedDuration, actualCmd.duration());
    assertEquals(expectedOpened, actualCmd.opened());
    assertEquals(expectedPublished, actualCmd.published());
    assertEquals(expectedRating, actualCmd.rating());
    assertEquals(expectedCategories, actualCmd.categories());
    assertEquals(expectedGenres, actualCmd.genres());
    assertEquals(expectedCastMembers, actualCmd.members());
    assertEquals(expectedVideoMock.getBytes(), actualCmd.video().getContent());
    assertEquals(expectedTrailerMock.getBytes(), actualCmd.trailer().getContent());
    assertEquals(expectedBannerMock.getBytes(), actualCmd.banner().getContent());
    assertEquals(expectedThumbnailMock.getBytes(), actualCmd.thumbnail().getContent());
    assertEquals(expectedThumbnailHalfMock.getBytes(), actualCmd.thumbnailHalf().getContent());

  }

}