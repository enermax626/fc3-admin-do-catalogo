package com.fullcycle.admin.catalogo.domain.exceptions;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException{


  protected NotFoundException(String message, List<Error> errors) {
    super(message, errors);
  }

  public static NotFoundException with(final Class<? extends AggregateRoot> anAggregate, final
      Identifier identifier) {

    final var anError = "%s with ID %s was not found".formatted(anAggregate.getSimpleName(), identifier.getValue());

    return new NotFoundException(anError, Collections.emptyList());
  }
}
