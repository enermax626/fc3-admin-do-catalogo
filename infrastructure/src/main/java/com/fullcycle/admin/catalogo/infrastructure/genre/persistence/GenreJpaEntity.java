package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "genre")
public class GenreJpaEntity {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<GenreCategoryJpaEntity> categories;

  @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant updatedAt;

  @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
  private Instant deletedAt;

  public GenreJpaEntity() {
  }

  private GenreJpaEntity(String id, String name, boolean isActive, Instant createdAt,
      Instant updatedAt, @Nullable Instant deletedAt) {
    this.id = id;
    this.name = name;
    this.isActive = isActive;
    this.categories = new HashSet<>();
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public static GenreJpaEntity from(Genre genre) {
    final var entity = new GenreJpaEntity(genre.getId().getValue(), genre.getName(),
        genre.getIsActive(),
        genre.getCreatedAt(), genre.getUpdatedAt(), genre.getDeletedAt());

    genre.getCategories().forEach(entity::addCategory);

    return entity;
  }

  public Genre toAggregate() {
    return Genre.with(this.getId(),
        this.getName(),
        this.getIsActive(),
        this.getCategoriesIds(),
        this.getCreatedAt(),
        this.getUpdatedAt(),
        this.getDeletedAt());
  }

  private void addCategory(CategoryId categoryId) {
    this.categories.add(GenreCategoryJpaEntity.from(categoryId, this));
  }

  private void removeCategory(CategoryId categoryId) {
    this.categories.remove(GenreCategoryJpaEntity.from(categoryId, this));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean active) {
    isActive = active;
  }

  public Set<CategoryId> getCategoriesIds() {
    return this.categories.stream().map(it -> CategoryId.from(it.getId().getCategoryId())).collect(
        Collectors.toSet());
  }

  public Set<GenreCategoryJpaEntity> getCategories() {
    return categories;
  }

  public void setCategories(
      Set<GenreCategoryJpaEntity> categories) {
    this.categories = categories;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Instant deletedAt) {
    this.deletedAt = deletedAt;
  }
}
