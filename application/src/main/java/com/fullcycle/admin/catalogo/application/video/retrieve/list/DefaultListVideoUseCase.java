package com.fullcycle.admin.catalogo.application.video.retrieve.list;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery;

public class DefaultListVideoUseCase extends ListVideoUseCase {

  private final VideoGateway videoGateway;

  public DefaultListVideoUseCase(VideoGateway videoGateway) {
    this.videoGateway = videoGateway;
  }

  @Override
  public Pagination<ListVideoOutput> execute(VideoSearchQuery input) {
    return videoGateway.findAll(input).map(ListVideoOutput::from);
  }
}
