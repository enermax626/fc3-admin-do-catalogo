package com.fullcycle.admin.catalogo.infrastructure.amqp;

import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderCompleted;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderError;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoEncoderResult;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class VideoEncoderListener {


  private static final Logger log = org.slf4j.LoggerFactory.getLogger(VideoEncoderListener.class);
  public static final String RABBIT_LISTENER_ID = "videoEncodedListener";
  private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

  public VideoEncoderListener(UpdateMediaStatusUseCase updateMediaStatusUseCase) {
    this.updateMediaStatusUseCase = updateMediaStatusUseCase;
  }

  @RabbitListener(id = RABBIT_LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
  public void onVideoEncodedMessage(@Payload String message) {
    VideoEncoderResult result = Json.readValue(message, VideoEncoderResult.class);

    if (result instanceof VideoEncoderCompleted dto) {
      VideoEncoderCompleted completed = (VideoEncoderCompleted) result;
      final var aCmd = UpdateMediaStatusCommand.with(MediaStatus.COMPLETED, dto.id(),
          dto.video().resourceId(), dto.video().encodedVideoFolder(), dto.video().filePath());
      updateMediaStatusUseCase.execute(aCmd);
    } else if (result instanceof VideoEncoderError error) {
      log.error("[message:video.listener.income] [status:ERROR] [payload:{}]", message);
    } else {
      log.error("[message:video.listener.income] [status:UNKNOWN] [payload:{}]", message);

    }

  }

}
