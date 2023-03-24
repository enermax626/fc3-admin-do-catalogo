package com.fullcycle.admin.catalogo.e2e.category;

import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
@AutoConfigureMockMvc
public class GenreE2ETest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private GenreRepository genreRepository;

  @Autowired
  private CategoryRepository categoryRepository;

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
  public void asCatalogAdminIShouldBeAbleToCreateAGenreWithValidValues() throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
//    Assertions.assertEquals(0, genreRepository.count());
//w
//    CategoryId categoryId =
//
//    Assertions.assertEquals(1, categoryRepository.count());
//
//    CategoryApiOutput categoryOutput = retrieveCategory(categoryId);
//
//    Assertions.assertEquals(categoryId.getValue(), categoryOutput.id());

  }














}
