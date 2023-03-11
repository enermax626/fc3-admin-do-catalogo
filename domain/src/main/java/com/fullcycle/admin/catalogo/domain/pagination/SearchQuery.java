package com.fullcycle.admin.catalogo.domain.pagination;

public record SearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {

  public static SearchQuery with(int page, int perPage, String terms, String sort, String direction) {
    return new SearchQuery(page, perPage, terms, sort, direction);
  }
}
