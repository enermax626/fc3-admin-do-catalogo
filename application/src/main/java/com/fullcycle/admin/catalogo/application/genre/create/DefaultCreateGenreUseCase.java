package com.fullcycle.admin.catalogo.application.genre.create;

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {

  private final GenreGateway genreGateway;

  private final CategoryGateway categoryGateway;

  public DefaultCreateGenreUseCase(GenreGateway genreGateway, CategoryGateway categoryGateway) {
    this.genreGateway = Objects.requireNonNull(genreGateway);
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
    ;
  }

  @Override
  public CreateGenreOutput execute(CreateGenreCommand input) {
    final var genreName = input.name();
    final var isActive = input.isActive();
    final var categoriesIds = toCategoryId(input.categories());

    final var notification = Notification.create();
    notification.append(validateCategories(categoriesIds));
    final var aGenre = notification.validate(() -> Genre.newGenre(genreName, isActive));
    categoriesIds.forEach(aGenre::addCategory);

    if(notification.hasError()) {
      throw new NotificationException("Could not create Aggregate Genre", notification);
    }

    return CreateGenreOutput.from(genreGateway.create(aGenre));
  }

  private Notification validateCategories(List<CategoryId> categoriesIds) {
    final var notification = Notification.create();
    if (categoriesIds == null || categoriesIds.isEmpty()) {
      return notification;
    }
    final var retrievedIds = categoryGateway.existsByIds(categoriesIds);
    if (categoriesIds.size() != retrievedIds.size()) {
      final var missingIds = new ArrayList<>(categoriesIds);
      missingIds.removeAll(retrievedIds);

      String missingIdsText = missingIds.stream().map(CategoryId::getValue)
          .collect(Collectors.joining(","));
      notification.append(
          new Error("Some categories were not found: %s".formatted(missingIdsText)));
    }
    return notification;
  }

  private List<CategoryId> toCategoryId(List<String> categories) {
    return categories.stream().map(CategoryId::from).collect(Collectors.toList());
  }
}
