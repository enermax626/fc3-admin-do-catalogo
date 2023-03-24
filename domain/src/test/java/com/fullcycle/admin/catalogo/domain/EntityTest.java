package com.fullcycle.admin.catalogo.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import com.fullcycle.admin.catalogo.domain.events.DomainEventPublisher;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class EntityTest {

  @Test
  void whenCallConstructor_withNullEvents_shouldBeOk() {
    // given
    List<DomainEvent> events = null;

    // when
    DummyEntity dummyEntity = new DummyEntity(new DummyIdentifier(), events);

    // then
    assertNotNull(dummyEntity);
    assertNotNull(dummyEntity.getDomainEvents());
    assertTrue(dummyEntity.getDomainEvents().isEmpty());
  }

  @Test
  void whenCallConstructor_withEventListPopulated_shouldReturnEntityWithEvents() {
    // given
    DomainEvent event = new DomainEvent() {
      @Override
      public Instant occurredOn() {
        return null;
      }
    };
    List<DomainEvent> events = List.of(event);

    // when
    DummyEntity dummyEntity = new DummyEntity(new DummyIdentifier(), events);

    // then
    assertNotNull(dummyEntity);
    assertNotNull(dummyEntity.getDomainEvents());
    assertFalse(dummyEntity.getDomainEvents().isEmpty());
    assertEquals(events.size(), dummyEntity.getDomainEvents().size());
  }

  @Test
  void whenCallGetDomainEvents_withEventListPopulated_shouldReturnEntityWithEventsUnmodifiable() {
    // given
    DomainEvent event = () -> null;
    AtomicInteger counter = new AtomicInteger(0);
    List<DomainEvent> events = List.of(event, event);
    DummyEntity dummyEntity = new DummyEntity(new DummyIdentifier(), events);
    DomainEventPublisher domainEventPublisher = event1 -> counter.incrementAndGet();

    // when
    dummyEntity.publishDomainEvents(domainEventPublisher);
    // then
    assertNotNull(dummyEntity);
    assertNotNull(dummyEntity.getDomainEvents());
    assertTrue(dummyEntity.getDomainEvents().isEmpty());
    assertEquals(events.size(), counter.get());
    assertThrows(UnsupportedOperationException.class,
        () -> dummyEntity.getDomainEvents().add(event));
  }

  public static class DummyIdentifier extends Identifier {

    private final String id;

    public DummyIdentifier() {
      id = IdUtils.generate();
    }

    @Override
    public String getValue() {
      return id;
    }
  }

  public static class DummyEntity extends Entity<Identifier> {


    public DummyEntity(Identifier identifier, List<DomainEvent> domainEvents) {
      super(identifier, domainEvents);
    }

    @Override
    public void validate(ValidationHandler validationHander) {
      // TODO Auto-generated method stub

    }

  }
}