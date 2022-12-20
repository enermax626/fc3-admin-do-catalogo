package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryId> implements Cloneable {

    private String name;

    private String description;

    private Boolean active;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    private Category(CategoryId id, String name, String description, Boolean active, Instant createdAt, Instant updatedAt, Instant deletedAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt should not be null");
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String expectedName, final String expectedDescription,
                                       final boolean isActive) {

        Instant now = Instant.now();
        final var deletedAt = isActive ? null : Instant.now();
        return new Category(CategoryId.unique(), expectedName, expectedDescription, isActive, now, now, deletedAt);
    }

    public static Category with(String anId,
                                String name,
                                String description,
                                boolean active,
                                Instant createdAt,
                                Instant updatedAt,
                                Instant deletedAt){
        return new Category(CategoryId.from(anId), name, description, active, createdAt, updatedAt, deletedAt);
    }

    @Override
    public void validate(ValidationHandler validationHander) {
        new CategoryValidator(validationHander, this).validate();
    }

    public Category update(String aName, String aDescription, boolean isActive) {

        if(isActive){
            this.activate();
        } else {
            this.deactivate();
        }
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate(){
        if(this.deletedAt == null) {
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public CategoryId getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
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

    @Override
    public Category clone() {
        try {
            Category clone = (Category) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}