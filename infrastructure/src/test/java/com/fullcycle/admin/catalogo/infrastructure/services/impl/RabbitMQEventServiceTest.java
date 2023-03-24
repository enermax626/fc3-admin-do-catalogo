package com.fullcycle.admin.catalogo.infrastructure.services.impl;

import com.fullcycle.admin.catalogo.AmqpTest;
import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.services.EventService;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness.InvocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@AmqpTest
class RabbitMQEventServiceTest {

  private static final String LISTENER = "videoCreatedListener";

  @Autowired
  @VideoCreatedQueue
  private EventService eventService;

  @Autowired
  private RabbitListenerTestHarness harness;


  @Test
  public void shouldSendMessage() throws InterruptedException {
    //given
    VideoCreatedEvent notification = new VideoCreatedEvent("resourceId", "filePath");

    final var expectedMesage = Json.writeValueAsString(notification);

    // when
    eventService.send(notification);

    // then
    InvocationData data = harness.getNextInvocationDataFor(LISTENER, 1,
        TimeUnit.SECONDS);

    Assertions.assertNotNull(data);
    Assertions.assertEquals(expectedMesage, data.getArguments()[0]);

  }


  @Component
  static class VideoCreatedListener {

    @RabbitListener(id = LISTENER, queues = "${amqp.queues.video-created.routing-key}")
    void onVideoCreated(@Payload String event) {

    }
  }

  private static class VideoCreatedEvent implements DomainEvent {

    private final String resourceId;
    private final String filePath;

    private VideoCreatedEvent(String resourceId, String filePath) {
      this.resourceId = resourceId;
      this.filePath = filePath;
    }

    public String getResourceId() {
      return resourceId;
    }

    public String getFilePath() {
      return filePath;
    }

    @Override
    public Instant occurredOn() {
      return null;
    }
  }

}