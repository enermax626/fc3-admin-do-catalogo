package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class GoogleStorageProperties implements InitializingBean {

  Logger log = LoggerFactory.getLogger(GoogleStorageProperties.class);

  private String bucketName;

  private int connectionTimeout;

  private int readTimeout;

  private int retryDelay;

  private int retryMaxAttempts;

  private int retryMaxDelay;

  private double retryMultiplier;

  @Override
  public void afterPropertiesSet() throws Exception {
    log.debug(this.toString());
  }

  //
  @Override
  public String toString() {
    return "GoogleStorageProperties{" +
        "bucketName='" + bucketName + '\'' +
        ", connectionTimeout=" + connectionTimeout +
        ", readTimeout=" + readTimeout +
        ", retryDelay=" + retryDelay +
        ", retryMaxAttempts=" + retryMaxAttempts +
        ", retryMaxDelay=" + retryMaxDelay +
        ", retryMultiplier=" + retryMultiplier +
        '}';
  }

  public Logger getLog() {
    return log;
  }

  public void setLog(Logger log) {
    this.log = log;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public int getRetryDelay() {
    return retryDelay;
  }

  public void setRetryDelay(int retryDelay) {
    this.retryDelay = retryDelay;
  }

  public int getRetryMaxAttempts() {
    return retryMaxAttempts;
  }

  public void setRetryMaxAttempts(int retryMaxAttempts) {
    this.retryMaxAttempts = retryMaxAttempts;
  }

  public int getRetryMaxDelay() {
    return retryMaxDelay;
  }

  public void setRetryMaxDelay(int retryMaxDelay) {
    this.retryMaxDelay = retryMaxDelay;
  }

  public double getRetryMultiplier() {
    return retryMultiplier;
  }

  public void setRetryMultiplier(double retryMultiplier) {
    this.retryMultiplier = retryMultiplier;
  }
}
