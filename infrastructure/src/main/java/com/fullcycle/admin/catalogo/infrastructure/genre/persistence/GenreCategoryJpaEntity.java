package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "genre_category")
public class GenreCategoryJpaEntity {

  @EmbeddedId
  private GenreCategoryId id;

  @ManyToOne
  @MapsId("genreId")
  private GenreJpaEntity genre;

  public GenreCategoryJpaEntity() {
  }

  private GenreCategoryJpaEntity(CategoryId id, GenreJpaEntity genre) {
    this.id = GenreCategoryId.from(genre.getId(), id.getValue());
    this.genre = genre;
  }

  public static GenreCategoryJpaEntity from(CategoryId id, GenreJpaEntity genre) {
    return new GenreCategoryJpaEntity(id, genre);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
    return id.equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public GenreCategoryId getId() {
    return id;
  }

  public void setId(GenreCategoryId id) {
    this.id = id;
  }

  public GenreJpaEntity getGenre() {
    return genre;
  }

  public void setGenre(GenreJpaEntity genre) {
    this.genre = genre;
  }
}
