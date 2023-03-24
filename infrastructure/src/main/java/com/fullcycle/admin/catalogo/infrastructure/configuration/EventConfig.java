package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fullcycle.admin.catalogo.infrastructure.services.EventService;
import com.fullcycle.admin.catalogo.infrastructure.services.impl.RabbitMQEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {


  @Bean
  @VideoCreatedQueue
  EventService videoCreatedEventService(@VideoCreatedQueue QueueProperties queueProperties, RabbitOperations rabbitOperations) {
    return new RabbitMQEventService(queueProperties.getExchange(), queueProperties.getRoutingKey(), rabbitOperations);
  }

}
