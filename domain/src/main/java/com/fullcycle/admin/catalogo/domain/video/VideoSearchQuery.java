package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberId;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.domain.genre.GenreId;
import java.util.Set;

public record VideoSearchQuery(int page, int perPage, String terms, String sort, String direction,
                               Set<CastMemberId> castMembers, Set<CategoryId> categories,
                               Set<GenreId> genres) {


  public static VideoSearchQuery with(int page, int perPage, String terms, String sort,
      String direction, Set<CastMemberId> castMembers, Set<CategoryId> categories,
      Set<GenreId> genres) {
    return new VideoSearchQuery(page, perPage, terms, sort, direction, castMembers, categories,
        genres);
  }
}
