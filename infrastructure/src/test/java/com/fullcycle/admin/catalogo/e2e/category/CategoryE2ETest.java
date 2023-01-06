package com.fullcycle.admin.catalogo.e2e.category;

import com.fullcycle.admin.catalogo.E2ETest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

  @Container
  public static final MySQLContainer MY_SQL_CONTAINER
      = new MySQLContainer("mysql:latest")
      .withPassword("123456")
      .withUsername("root")
      .withDatabaseName("adm_videos");

  @DynamicPropertySource
  public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
    final var mappedPort = MY_SQL_CONTAINER.getMappedPort(3306);
    registry.add("mysql.port", () -> mappedPort);
  }

  @Test
  public void works() {
    Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
  }
}
