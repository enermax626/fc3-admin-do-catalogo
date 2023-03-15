package com.fullcycle.admin.catalogo;

import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class MySQLCleanUpExtension implements BeforeEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    ApplicationContext applicationContext = SpringExtension
        .getApplicationContext(context);

    List.of(
        applicationContext.getBean(GenreRepository.class),
        applicationContext.getBean(CategoryRepository.class)
    ).forEach(CrudRepository::deleteAll);

  }
}
