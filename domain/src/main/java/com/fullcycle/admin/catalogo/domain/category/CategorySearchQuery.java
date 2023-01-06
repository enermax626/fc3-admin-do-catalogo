package com.fullcycle.admin.catalogo.domain.category;

public record CategorySearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {

  public static CategorySearchQuery with(int page, int perPage, String terms, String sort, String direction) {
    return new CategorySearchQuery(page, perPage, terms, sort, direction);
  }
}
