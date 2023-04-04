package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases;

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.create.DefaultCreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.media.get.DefaultGetMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.DefaultGetVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.list.DefaultListVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.update.DefaultUpdateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoUseCaseConfig {

  private final VideoGateway videoGateway;
  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;
  private final CastMemberGateway castMemberGateway;
  private final MediaResourceGateway mediaResourceGateway;


  public VideoUseCaseConfig(VideoGateway videoGateway, CategoryGateway categoryGateway,
      GenreGateway genreGateway, CastMemberGateway castMemberGateway,
      MediaResourceGateway mediaResourceGateway) {
    this.videoGateway = videoGateway;
    this.categoryGateway = categoryGateway;
    this.genreGateway = genreGateway;
    this.castMemberGateway = castMemberGateway;
    this.mediaResourceGateway = mediaResourceGateway;
  }

  @Bean
  public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
    return new DefaultUpdateMediaStatusUseCase(videoGateway);
  }

  @Bean
  public GetMediaUseCase getMediaUseCase() {
    return new DefaultGetMediaUseCase(mediaResourceGateway);
  }

  @Bean
  public UploadMediaUseCase uploadMediaUseCase() {
    return new DefaultUploadMediaUseCase(mediaResourceGateway, videoGateway);
  }

  @Bean
  public CreateVideoUseCase createVideoUseCase() {
    return new DefaultCreateVideoUseCase(videoGateway, categoryGateway, genreGateway,
        castMemberGateway, mediaResourceGateway);
  }

  @Bean
  public UpdateVideoUseCase updateVideoUseCase() {
    return new DefaultUpdateVideoUseCase(videoGateway, categoryGateway, castMemberGateway,
        genreGateway, mediaResourceGateway);
  }

  @Bean
  public GetVideoUseCase getVideoUseCase() {
    return new DefaultGetVideoUseCase(videoGateway);
  }

  @Bean
  public ListVideoUseCase listVideoUseCase() {
    return new DefaultListVideoUseCase(videoGateway);
  }

  @Bean
  public DeleteVideoUseCase deleteVideoUseCase() {
    return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
  }


}
