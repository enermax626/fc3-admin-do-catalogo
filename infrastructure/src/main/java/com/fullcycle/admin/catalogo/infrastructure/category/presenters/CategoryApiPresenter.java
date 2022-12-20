package com.fullcycle.admin.catalogo.infrastructure.category.presenters;

import com.fullcycle.admin.catalogo.application.category.retreive.get.CategoryOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryApiOutput;

public interface CategoryApiPresenter {

  static CategoryApiOutput present(CategoryOutput aCategoryOutput){
    return new CategoryApiOutput(
        aCategoryOutput.id().getValue(),
        aCategoryOutput.name(),
        aCategoryOutput.description(),
        aCategoryOutput.isActive(),
        aCategoryOutput.createdAt(),
        aCategoryOutput.updatedAt(),
        aCategoryOutput.deletedAt()
    );
  }


}
