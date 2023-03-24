package com.fullcycle.admin.catalogo.infrastructure.configuration;


import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google.GoogleCloudProperties;
import com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.threeten.bp.Duration;

@Configuration
@Profile({"development", "production"})
public class GoogleCloudConfig {

  @Bean
  @ConfigurationProperties(prefix = "google.cloud")
  public GoogleCloudProperties googleCloudProperties() {
    return new GoogleCloudProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "google.cloud.storage.catalogo-videos")
  public GoogleStorageProperties googleStorageProperties() {
    return new GoogleStorageProperties();
  }

  @Bean
  public Credentials credentials(GoogleCloudProperties googleCloudProperties) {
    byte[] decodedCredentials = Base64.getDecoder().decode(googleCloudProperties.getCredentials());
    try {
//      Json.INSTANCE.mapper().readValue(decodedCredentials, ServiceAccountCredentials.class);
      return GoogleCredentials.fromStream(new ByteArrayInputStream(decodedCredentials));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Bean
  public Storage storage(Credentials credentials, GoogleStorageProperties googleStorageProperties) {

    HttpTransportOptions transportOptions = HttpTransportOptions.newBuilder()
        .setConnectTimeout(googleStorageProperties.getConnectionTimeout())
        .setReadTimeout(googleStorageProperties.getReadTimeout())
        .build();

    RetrySettings retrySettings = RetrySettings.newBuilder()
        .setInitialRetryDelay(Duration.ofMillis(googleStorageProperties.getRetryDelay()))
        .setMaxRetryDelay(Duration.ofMillis(googleStorageProperties.getRetryMaxDelay()))
        .setMaxAttempts(googleStorageProperties.getRetryMaxAttempts())
        .setRetryDelayMultiplier(googleStorageProperties.getRetryMultiplier())
        .build();

    StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials)
        .setTransportOptions(transportOptions)
        .setRetrySettings(retrySettings)
        .build();

    return options.getService();
  }


}
