package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.Objects;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand input) {
        final var anId = CategoryId.from(input.id());
        final var aName = input.name();
        final var aDescription = input.description();
        final var isActive = input.isActive();

        final var notification = Notification.create();

        Category aCategory = categoryGateway.findById(anId)
                .orElseThrow(() ->
                        DomainException.with(new Error("Category with Id %s was not found".formatted(anId.getValue()))));

        aCategory.update(aName, aDescription, isActive)
                .validate(notification);

        return notification.hasError() ? API.Left(notification) : update(aCategory);

    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return Try.of(() -> categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
