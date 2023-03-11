package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

public class GenreValidator extends Validator {
  public static final int MIN_NAME_LENGTH = 3;
  public static final int MAX_NAME_LENGTH = 255;
  private final Genre genre;

  protected GenreValidator(
      ValidationHandler aHandler,
      Genre genre) {
    super(aHandler);
    this.genre = genre;
  }

  @Override
  public void validate() {
    checkNameConstraints();
  }

  private void checkNameConstraints() {
    final var name = this.genre.getName();
    if(name == null) {
      this.validationHandler().append(new Error("'name' should not be null"));
      return;
    }

    if(name.isBlank()) {
      this.validationHandler().append(new Error("'name' should not be empty"));
      return;
    }
    final var length = name.trim().length();
    if(length < MIN_NAME_LENGTH || length > MAX_NAME_LENGTH) {
      this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
    }
  }
}
