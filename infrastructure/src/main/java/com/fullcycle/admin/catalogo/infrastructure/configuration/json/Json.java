package com.fullcycle.admin.catalogo.infrastructure.configuration.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import java.util.concurrent.Callable;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public enum Json {

  INSTANCE;

  public static ObjectMapper mapper(){
    return INSTANCE.mapper.copy();
  }

  public static String writeValueAsString(Object obj) {
    return invoke(() -> INSTANCE.mapper.writeValueAsString(obj));
  }

  public static <T> T readValue(String stringValue, Class<T> clazz) {
      return invoke(() -> INSTANCE.mapper.readValue(stringValue, clazz));
  }

  public static <T> T readValue(String stringValue, TypeReference<T> clazz) {
    return invoke(() -> INSTANCE.mapper.readValue(stringValue, clazz));
  }

  private static <T> T invoke(Callable<T> value) {
    try {
      return value.call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
      .dateFormat(new StdDateFormat())
      .featuresToDisable(
          DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
          DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
          DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
          SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
      )
//      .featuresToEnable(Include.NON_NULL)
      .modules(new JavaTimeModule(), new Jdk8Module(), afterBurnerModule())
      .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .build()
      .setDefaultPropertyInclusion(Include.NON_NULL);

  private Module afterBurnerModule() {
    final var module = new AfterburnerModule();
    module.setUseValueClassLoader(false);
    return module;
  }
}
