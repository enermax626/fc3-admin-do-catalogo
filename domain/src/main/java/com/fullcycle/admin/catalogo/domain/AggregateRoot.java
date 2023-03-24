package com.fullcycle.admin.catalogo.domain;

import com.fullcycle.admin.catalogo.domain.events.DomainEvent;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID>{

    public AggregateRoot(ID identifier) {
        this(identifier, Collections.emptyList());
    }

    public AggregateRoot(ID identifier, List<DomainEvent> domainEvents) {
        super(identifier, domainEvents);
    }
}
