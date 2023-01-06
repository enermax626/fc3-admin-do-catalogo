package com.fullcycle.admin.catalogo;

import com.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class )
@ExtendWith(MySQLCleanUpExtension.class)
public @interface IntegrationTest {

  class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
      final var repositories = SpringExtension
          .getApplicationContext(context)
          .getBeansOfType(CrudRepository.class)
          .values();

      repositories.forEach(CrudRepository::deleteAll);
    }
  }
}