package com.fullcycle.admin.catalogo.infrastructure.category.presenters;

import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListApiOutput;

public class GenreApiPresenter {

  public static GenreApiOutput present(GenreOutput aGenreOutput){
    return new GenreApiOutput(
        aGenreOutput.id(),
        aGenreOutput.name(),
        aGenreOutput.categories(),
        aGenreOutput.isActive(),
        aGenreOutput.createdAt(),
        aGenreOutput.updatedAt(),
        aGenreOutput.deletedAt()
    );
  }

  public static GenreListApiOutput presentList(GenreListOutput aGenreOutput){
    return new GenreListApiOutput(
        aGenreOutput.id(),
        aGenreOutput.name(),
        aGenreOutput.categories(),
        aGenreOutput.isActive(),
        aGenreOutput.createdAt(),
        aGenreOutput.deletedAt()
    );
  }

}
