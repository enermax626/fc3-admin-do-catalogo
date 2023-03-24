package com.fullcycle.admin.catalogo.infrastructure.amqp;

import static com.fullcycle.admin.catalogo.infrastructure.amqp.VideoEncoderListener.RABBIT_LISTENER_ID;
import static org.junit.jupiter.api.Assertions.*;

import com.fullcycle.admin.catalogo.AmqpTest;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderError;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoMessage;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness.InvocationData;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@AmqpTest
class VideoEncoderListenerTest {

  @Autowired
  private TestRabbitTemplate testRabbitTemplate;

  @Autowired
  private RabbitListenerTestHarness harness;

  @MockBean
  private UpdateMediaStatusUseCase updateMediaStatusUseCase;

  @Autowired
  @VideoEncodedQueue
  private QueueProperties queueProperties;

  @Test
  void onVideoEncodedMessage() throws InterruptedException {
    // given
    final var expectedError = new VideoEncoderError(
        new VideoMessage("123", "a-file-path"),
        "Video not found");

    final var expectedMessage = Json.writeValueAsString(expectedError);

    // when
    testRabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);


  // then
    final var invocationData = harness.getNextInvocationDataFor(RABBIT_LISTENER_ID, 2,
        TimeUnit.SECONDS);

    assertNotNull(invocationData);
    final var actualMessage = (String) invocationData.getArguments()[0];
    assertEquals(expectedMessage, actualMessage);
  }

}