package com.fullcycle.admin.catalogo.infrastructure.video.models;

import static org.junit.jupiter.api.Assertions.*;

import com.fullcycle.admin.catalogo.JacksonTest;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;

@JacksonTest
class VideoEncoderResultTest {

  @Autowired
  private JacksonTester<VideoEncoderResult> json;

  @Test
  public void testUnmarshalSuccessResult() throws IOException {
    //given
    final var expectedId = IdUtils.generate();
    final var expectedOutputBucket = "gs://bucket";
    final var expectedStatus = "COMPLETED";
    final var expectedEncoderVideoFolder = "encoder-video-folder";
    final var expecetedResourceId = IdUtils.generate();
    final var expectedFilePath = "file-path.mp4";
    final var expectedMetaData = new VideoMetaData(expectedEncoderVideoFolder, expecetedResourceId,
        expectedFilePath);

    final var json = """
        {
          "id": "%s",
          "output_bucket_path": "%s",
          "status": "%s",
          "video" : {
            "encoded_video_folder": "%s",
            "resource_id": "%s",
            "file_path": "%s"
          }
        }
        """.formatted(expectedId, expectedOutputBucket, expectedStatus, expectedEncoderVideoFolder,
        expecetedResourceId, expectedFilePath);

    // when
    ObjectContent<VideoEncoderResult> result = this.json.parse(json);

    //then
    Assertions.assertThat(result).isNotNull()
        .isInstanceOf(VideoEncoderCompleted.class)
        .hasFieldOrPropertyWithValue("id", expectedId)
        .hasFieldOrPropertyWithValue("outputBucket", expectedOutputBucket)
        .hasFieldOrPropertyWithValue("status", expectedStatus)
        .hasFieldOrPropertyWithValue("video", expectedMetaData);
  }

  @Test
  public void testUnmarshalErrorResult() throws IOException {
    //given
    final var expectedError = "Not able to encode video";
    final var expectedStatus = "ERROR";
    final var expectedResourceId = IdUtils.generate();
    final var expectedFilePath = "file-path.mp4";
    final var expectedMessage = new VideoMessage(expectedResourceId, expectedFilePath);

    final var json = """
        {
          "status": "%s",
          "error": "%s",
          "message": {
            "resource_id": "%s",
            "file_path": "%s"
          }
        }
        """.formatted(expectedStatus, expectedError, expectedResourceId, expectedFilePath);

    // when
    ObjectContent<VideoEncoderResult> result = this.json.parse(json);

    //then
    Assertions.assertThat(result).isNotNull()
        .isInstanceOf(VideoEncoderError.class)
        .hasFieldOrPropertyWithValue("error", expectedError)
        .hasFieldOrPropertyWithValue("message", expectedMessage);
  }
}