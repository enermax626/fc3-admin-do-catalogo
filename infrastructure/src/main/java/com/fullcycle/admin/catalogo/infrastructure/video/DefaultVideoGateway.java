package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoId;
import com.fullcycle.admin.catalogo.domain.video.VideoPreview;
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.fullcycle.admin.catalogo.infrastructure.services.EventService;
import com.fullcycle.admin.catalogo.infrastructure.utils.SqlUtils;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DefaultVideoGateway implements VideoGateway {

  private final VideoRepository videoRepository;

  private final EventService eventService;

  public DefaultVideoGateway(VideoRepository videoRepository,
      @VideoCreatedQueue EventService eventService) {
    this.videoRepository = videoRepository;
    this.eventService = eventService;
  }

  @Override
  @Transactional
  public Video create(Video video) {
    return save(video);
  }


  @Override
  public void deleteById(VideoId id) {
    final var aVideoId = id.getValue();
    if (this.videoRepository.existsById(aVideoId)) {
      this.videoRepository.deleteById(aVideoId);
    }

  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Video> findById(VideoId id) {
    return this.videoRepository.findById(id.getValue()).map(VideoJpaEntity::toAggregate);
  }

  @Override
  @Transactional
  public Video update(Video video) {
    return save(video);
  }

  @Override
  public Pagination<VideoPreview> findAll(VideoSearchQuery searchQuery) {
    PageRequest page = PageRequest.of(searchQuery.page(), searchQuery.perPage(),
        Sort.by(Direction.fromString(searchQuery.direction()), searchQuery.sort()));

    var videos = this.videoRepository.findAll(SqlUtils.like(searchQuery.terms()),
        toString(searchQuery.castMembers()), toString(searchQuery.genres()),
        toString(searchQuery.categories()), page);

    return new Pagination<>(videos.getNumber(), videos.getSize(), videos.getTotalElements(),
        videos.toList());

  }

  private Set<String> toString(Set<? extends Identifier> identifiers) {
    if (identifiers == null || identifiers.isEmpty()) {
      return null;
    }
    return identifiers.stream().map(Identifier::getValue).collect(Collectors.toSet());
  }

  private Video save(Video video) {
    Video result = this.videoRepository.save(VideoJpaEntity.from(video)).toAggregate();
    video.publishDomainEvents(eventService::send);
    return result;
  }
}
