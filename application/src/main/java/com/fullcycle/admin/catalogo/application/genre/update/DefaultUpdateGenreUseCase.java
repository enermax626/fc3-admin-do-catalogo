package com.fullcycle.admin.catalogo.application.genre.update;

import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUsecase {

  private final GenreGateway genreGateway;

  private final CategoryGateway categoryGateway;

  public DefaultUpdateGenreUseCase(GenreGateway genreGateway, CategoryGateway categoryGateway) {
    this.genreGateway = genreGateway;
    this.categoryGateway = categoryGateway;
  }

  @Override
  public UpdateGenreOutput execute(UpdateGenreCommand aCommand) {
    final var anId = GenreId.from(aCommand.id());
    final var aName = aCommand.name();
    final var isActive = aCommand.isActive();
    final var categories = aCommand.categories().stream().map(CategoryId::from)
        .collect(Collectors.toSet());

    Genre aGenre = genreGateway.findById(anId)
        .orElseThrow(() -> NotFoundException.with(Genre.class, anId));

    Notification notification = Notification.create();
    notification.append(validateCategories(categories));
    Genre updatedGenre = notification.validate(() -> aGenre.update(aName, isActive, categories));

    if (notification.hasError()) {
      throw new NotificationException("Could not create Aggregate Genre", notification);
    }

    return UpdateGenreOutput.from(genreGateway.update(updatedGenre));
  }

  private Notification validateCategories(Set<CategoryId> categoriesIds) {
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
}
