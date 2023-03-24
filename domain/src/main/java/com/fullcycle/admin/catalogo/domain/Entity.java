package com.fullcycle.admin.catalogo.domain;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.domain.events.DomainEventPublisher;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> {

  protected final ID id;
  private final List<DomainEvent> domainEvents;

  public Entity(final ID id, List<DomainEvent> domainEvents) {
    this.domainEvents = new ArrayList<>(domainEvents == null ? new ArrayList<>() : domainEvents);
    Objects.requireNonNull(id, "id should not be null");
    this.id = id;
  }

  public abstract void validate(ValidationHandler validationHander);

  public ID getId() {
    return id;
  }

  public List<DomainEvent> getDomainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  public void publishDomainEvents(DomainEventPublisher domainEventPublisher) {
    if (domainEventPublisher == null) {
      return;
    }
    domainEvents.forEach(domainEventPublisher::publishEvent);
    this.domainEvents.clear();
  }

  public void registerEvent(DomainEvent domainEvent) {
    if(domainEvent != null) {
      this.domainEvents.add(domainEvent);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entity<?> entity = (Entity<?>) o;
    return getId().equals(entity.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
