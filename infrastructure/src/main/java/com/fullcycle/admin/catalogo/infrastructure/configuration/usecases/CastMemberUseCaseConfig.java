package com.fullcycle.admin.catalogo.infrastructure.configuration.usecases;

import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.GetCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberUsecase;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CastMemberUseCaseConfig {

  private final CastMemberGateway castMemberGateway;

  public CastMemberUseCaseConfig(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = castMemberGateway;
  }

  @Bean
  public CreateCastMemberUseCase createCastMemberUseCase() {
    return new DefaultCreateCastMemberUseCase(castMemberGateway);
  }

  @Bean
  public DefaultDeleteCastMemberUseCase deleteCastMemberUseCase() {
    return new DefaultDeleteCastMemberUseCase(castMemberGateway);
  }

  @Bean
  public UpdateCastMemberUsecase updateCastMemberUseCase() {
    return new DefaultUpdateCastMemberUseCase(castMemberGateway);
  }

  @Bean
  public GetCastMemberUseCase getCastMemberUseCase() {
    return new DefaultGetCastMemberUseCase(castMemberGateway);
  }

  @Bean
  public ListCastMemberUseCase listCastMemberUseCase() {
    return new DefaultListCastMemberUseCase(castMemberGateway);
  }
}
