package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.GetCastMemberOutput;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.GetCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberUsecase;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastmemberOutput;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.api.CastMemberAPI;
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CreateCastMemberApiInput;
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.CreateCastMemberApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.GetCastMemberApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberApiInput;
import com.fullcycle.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberApiOutput;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CastMemberController implements CastMemberAPI {

  private final CreateCastMemberUseCase createCastMemberUseCase;
  private final UpdateCastMemberUsecase updateCastMemberUsecase;
  private final DeleteCastMemberUseCase deleteCastMemberUseCase;
  private final GetCastMemberUseCase getCastMemberUseCase;
  private final ListCastMemberUseCase listCastMemberUseCase;

  public CastMemberController(CreateCastMemberUseCase createCastMemberUseCase,
      UpdateCastMemberUsecase updateCastMemberUsecase,
      DeleteCastMemberUseCase deleteCastMemberUseCase, GetCastMemberUseCase getCastMemberUseCase,
      ListCastMemberUseCase listCastMemberUseCase) {
    this.createCastMemberUseCase = createCastMemberUseCase;
    this.updateCastMemberUsecase = updateCastMemberUsecase;
    this.deleteCastMemberUseCase = deleteCastMemberUseCase;
    this.getCastMemberUseCase = getCastMemberUseCase;
    this.listCastMemberUseCase = listCastMemberUseCase;
  }

  @Override
  public ResponseEntity<?> updateById(String id, UpdateCastMemberApiInput anInput) {
    final var aCommand = UpdateCastMemberCommand.with(id, anInput.name(), anInput.type());
    UpdateCastmemberOutput result = updateCastMemberUsecase.execute(aCommand);
    return ResponseEntity.ok(UpdateCastMemberApiOutput.from(result));
  }

  @Override
  public void deleteById(String id) {
    this.deleteCastMemberUseCase.execute(id);
  }

  @Override
  public ResponseEntity<?> createCastMember(CreateCastMemberApiInput anInput) {
    final var aCommand = CreateCastMemberCommand.with(anInput.name(), anInput.type());
    CreateCastMemberOutput result = this.createCastMemberUseCase.execute(aCommand);

    return ResponseEntity.created(URI.create("/cast_members/" + result.id()))
        .body(CreateCastMemberApiOutput.from(result));
  }

  @Override
  public Pagination<?> listCastMembers(String search, int page, int perPage, String sort,
      String direction) {
    final var aSearchQuery = SearchQuery.with(
        page,
        perPage,
        search,
        sort,
        direction
    );

    return this.listCastMemberUseCase.execute(aSearchQuery);
  }

  @Override
  public GetCastMemberApiOutput getById(String id) {
    GetCastMemberOutput result = this.getCastMemberUseCase.execute(id);
    return GetCastMemberApiOutput.from(result);
  }
}
