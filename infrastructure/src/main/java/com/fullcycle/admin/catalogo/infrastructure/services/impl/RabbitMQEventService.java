package com.fullcycle.admin.catalogo.infrastructure.services.impl;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.services.EventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;

public class RabbitMQEventService implements EventService {

  private final String exchange;
  private final String routingKey;
  private final RabbitOperations rabbitOperations;

  public RabbitMQEventService(String exchange, String routingKey, RabbitOperations rabbitOperations) {
    this.exchange = exchange;
    this.routingKey = routingKey;
    this.rabbitOperations = rabbitOperations;
  }

  @Override
  public void send(DomainEvent event) {
    rabbitOperations.convertAndSend(exchange, routingKey, Json.writeValueAsString(event));

  }
}
