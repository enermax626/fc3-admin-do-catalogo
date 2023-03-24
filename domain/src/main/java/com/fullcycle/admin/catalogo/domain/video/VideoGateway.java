package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import java.util.Optional;

public interface VideoGateway {

  Video create(Video video);

  void deleteById(VideoId id);

  Optional<Video> findById(VideoId id);

  Video update(Video video);

  Pagination<VideoPreview> findAll(VideoSearchQuery searchQuery);


}
