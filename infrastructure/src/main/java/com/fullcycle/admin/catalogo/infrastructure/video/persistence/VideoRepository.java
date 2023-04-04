package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.video.VideoPreview;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {


  @Query("""
          select new com.fullcycle.admin.catalogo.domain.video.VideoPreview(
            v.id as id,
            v.title as title,
            v.description as description,
            v.createdAt as createdAt,
            v.updatedAt as updatedAt
          )
          from VideoJpaEntity v
          left join v.members members
          left join v.categories categories
          left join v.genres genres
          where
              (:terms is null or UPPER(v.title) like :terms)
          AND
              (:castMembers is null or members.id.castMemberId in :castMembers)
          AND
              (:categories is null or categories.id.categoryId in :categories)
          AND
              (:genres is null or genres.id.genreId in :genres)
      """)
  Page<VideoPreview> findAll(@Param("terms") String terms,
      @Param("castMembers") Set<String> castMembers, @Param("genres") Set<String> genres,
      @Param("categories") Set<String> categories, Pageable page);
}
