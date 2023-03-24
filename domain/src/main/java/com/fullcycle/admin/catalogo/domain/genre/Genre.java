package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Genre extends AggregateRoot<GenreId> {

  private String name;

  private boolean isActive;

  private Set<CategoryId> categories;

  private Instant createdAt;

  private Instant updatedAt;

  private Instant deletedAt;

  private Genre(GenreId identifier, String name, boolean isActive, Set<CategoryId> categories,
      Instant createdAt, Instant updatedAt, Instant deletedAt) {
    super(identifier);
    this.name = name;
    this.isActive = isActive;
    this.categories = categories;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
    selfValidate();
  }

  public static Genre newGenre(String name, boolean isActive) {
    final var deletedAt = isActive ? null : Instant.now();
    return new Genre(GenreId.unique(), name, isActive, new HashSet<>(), Instant.now(),
        Instant.now(), deletedAt);
  }

  public static Genre with(String anId, String name, boolean isActive, Set<CategoryId> categories,
      Instant createdAt, Instant updatedAt, Instant deletedAt) {
    return new Genre(GenreId.from(anId), name, isActive, categories, createdAt, updatedAt,
        deletedAt);
  }


  @Override
  public void validate(ValidationHandler validationHandler) {
    new GenreValidator(validationHandler, this).validate();
  }

  public String getName() {
    return name;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public Set<CategoryId> getCategories() {
    return Collections.unmodifiableSet(categories);
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public Genre deactivate() {
    if (this.deletedAt == null) {
      this.deletedAt = InstantUtils.now();
    }
    this.isActive = false;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Genre activate() {
    this.deletedAt = null;
    this.updatedAt = InstantUtils.now();
    this.isActive = true;
    return this;
  }

  public Genre update(String name, boolean isActive, Set<CategoryId> categories) {
    if (isActive) {
      activate();
    } else {
      deactivate();

    }
    this.name = name;
    this.categories = new HashSet<>(categories != null ? categories : Collections.emptySet());
    this.updatedAt = InstantUtils.now();
    selfValidate();
    return this;
  }

  private void selfValidate() {
    Notification notification = Notification.create();
    validate(notification);

    if (notification.hasError()) {
      throw new NotificationException("Failed to create an Aggregate Genre", notification);
    }
  }

  public Genre removeCategory(CategoryId categoryId) {
    if(categoryId == null || categoryId.getValue() == null) {
      return this;
    }
    this.categories.remove(categoryId);
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Genre addCategory(List<CategoryId> categories) {
    if(categories == null || categories.isEmpty()) {
      return this;
    }
    categories.forEach(this::addCategory);
    return this;
  }

  public Genre addCategory(CategoryId categoryId) {
    if(categoryId == null || categoryId.getValue() == null || this.categories.contains(categoryId)) {
      return this;
    }
    this.categories.add(categoryId);
    this.updatedAt = InstantUtils.now();
    return this;
  }
}
