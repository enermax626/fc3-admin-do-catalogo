package com.fullcycle.admin.catalogo.e2e.category;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.domain.category.CategoryId;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
@AutoConfigureMockMvc
public class CategoryE2ETest {

  @Autowired
  private MockMvc mvc;

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
  public void asCatalogAdminIShouldBeAbleToCreateACategoryWithValidValues() throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, categoryRepository.count());

    CategoryId categoryId = givenACategory(expectedName, expectedDescription, expectedIsActive);

    Assertions.assertEquals(1, categoryRepository.count());

    CategoryApiOutput categoryOutput = retrieveCategory(categoryId);

    Assertions.assertEquals(categoryId.getValue(), categoryOutput.id());

  }

  @Test
  public void asCatalogAdminIShouldBeAbleToListCategories() throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedName2 = "Series";
    final var expectedDescription2 = "A categoria menos assistida";
    final var expectedIsActive2 = true;

    CategoryId categoryId1 = givenACategory(expectedName, expectedDescription, expectedIsActive);
    CategoryId categoryId2 = givenACategory(expectedName2, expectedDescription2, expectedIsActive2);

    var responseBody = mvc.perform(get("/category"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items").isArray())
        .andExpect(jsonPath("$.items").isNotEmpty())
        .andExpect(jsonPath("$.items[0].id").value(categoryId1.getValue()));
  }


  private CategoryId givenACategory(String name, String description, Boolean isActive)
      throws Exception {

    MvcResult result = this.mvc.perform(post("/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"name\": \"" + name + "\", \"description\": \"" + description + "\", \"isActive\": "
                    + isActive + "}"))
        .andExpect(status().isCreated())
        .andReturn();

    final var categoryId = result.getResponse().getHeader("Location")
        .replace("/categories/", "");

    return CategoryId.from(categoryId);

  }

  private CategoryApiOutput retrieveCategory(CategoryId categoryId) throws Exception {
    var responseBody = mvc.perform(get("/category/" + categoryId.getValue()))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    return Json.readValue(responseBody, CategoryApiOutput.class);
  }


}
