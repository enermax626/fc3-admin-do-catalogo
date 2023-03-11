package com.fullcycle.admin.catalogo.infrastructure.genre.persistence;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GenreCategoryId implements Serializable {

  private static final long serialVersionUID = 6895852579158927361L;
  @Column(name = "genre_id")
  private String genreId;

  @Column(name = "category_id")
  private String categoryId;

  public GenreCategoryId() {
  }

  private GenreCategoryId(String genreId, String categoryId) {
    this.genreId = genreId;
    this.categoryId = categoryId;
  }

  public static GenreCategoryId from(String genreId, String categoryId) {
    return new GenreCategoryId(genreId, categoryId);
  }

  public String getGenreId() {
    return genreId;
  }

  public void setGenreId(String genreId) {
    this.genreId = genreId;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenreCategoryId that = (GenreCategoryId) o;
    return Objects.equals(genreId, that.genreId) && Objects.equals(categoryId,
        that.categoryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(genreId, categoryId);
  }
}
