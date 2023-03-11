package com.fullcycle.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler validationHandler);

    <T> T validate(Validation<T> aValidation);

    List<Error> getErrors();

    default boolean hasError(){
        return getErrors() != null && !this.getErrors().isEmpty();
    }

    interface Validation<T> {
        T validate();
    }
}
