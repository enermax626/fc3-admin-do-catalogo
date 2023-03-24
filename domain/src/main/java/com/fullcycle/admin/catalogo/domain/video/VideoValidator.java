package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

public class VideoValidator extends Validator {

  private static final int TITLE_MAX_LENGTH =255;
  private static final int DESCRIPTION_MAX_LENGTH = 255;
  private static final int TITLE_MIN_LENGTH = 3;

  private final Video video;

  protected VideoValidator(
      Video video,
      ValidationHandler aHandler) {
    super(aHandler);
    this.video = video;
  }

  @Override
  public void validate() {
    checkTitleConstraints();
    checkDescriptionConstraints();
    checkLaunchedAtConstraints();
    checkRatingConstraints();
  }

  private void checkTitleConstraints() {
    final var title = video.getTitle();

    if (title == null || title.isBlank()) {
      addError("Title is required");
    }

    final var length = title.trim().length();

    if(length > TITLE_MAX_LENGTH || length < TITLE_MIN_LENGTH) {
      addError("Title must be between " + TITLE_MIN_LENGTH + " and " + TITLE_MAX_LENGTH + " characters");
    }
  }

  private void checkDescriptionConstraints() {
    final var description = video.getDescription();

    if (description == null || description.isBlank()) {
      addError("Description is required");
    }

    final var length = description.trim().length();

    if(length > DESCRIPTION_MAX_LENGTH) {
      addError("Description must be less than " + DESCRIPTION_MAX_LENGTH + " characters");
    }
  }

  private void checkLaunchedAtConstraints() {
    if (video.getLaunchedAt() == null) {
      addError("LaunchedAt is required");
    }
  }

  private void checkRatingConstraints() {
    if (video.getRating() == null) {
      addError("Rating is required");
    }
  }

  private void addError(String errorMessage) {
    this.validationHandler().append(new Error(errorMessage));
  }
}
