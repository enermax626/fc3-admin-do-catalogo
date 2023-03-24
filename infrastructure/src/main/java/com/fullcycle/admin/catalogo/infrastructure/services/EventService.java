package com.fullcycle.admin.catalogo.infrastructure.services;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;

public interface EventService {

  void send(DomainEvent event); // could be a Object, but we want to be sure that we are sending a DomainEvent

}
