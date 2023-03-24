package com.fullcycle.admin.catalogo.infrastructure.configuration;

import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;
import com.fullcycle.admin.catalogo.infrastructure.services.impl.GoogleStorageService;
import com.fullcycle.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

  @Bean
  @ConfigurationProperties(prefix = "storage.catalogo-videos")
  public StorageProperties storageProperties() {
    return new StorageProperties();
  }

  @Bean(name = "storageService")
  @Profile({"development", "production"})
  public StorageService googleStorageService(GoogleStorageProperties googleStorageProperties,
      Storage storage) {
    return new GoogleStorageService(googleStorageProperties.getBucketName(), storage);
  }

  @Bean(name = "storageService")
  @ConditionalOnMissingBean
  public StorageService inMemoryStorageService() {
    return new InMemoryStorageService();
  }

}
