package com.fullcycle.admin.catalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class MySQLCleanUpExtension implements BeforeEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    final var repositories = SpringExtension
        .getApplicationContext(context)
        .getBeansOfType(CrudRepository.class)
        .values();

    repositories.forEach(CrudRepository::deleteAll);
  }
}
