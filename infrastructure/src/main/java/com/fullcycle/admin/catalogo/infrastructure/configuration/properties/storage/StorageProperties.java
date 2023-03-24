package com.fullcycle.admin.catalogo.infrastructure.configuration.properties.storage;

public class StorageProperties {

  private String locationPattern;
  private String fileNamePattern;

  public StorageProperties() {
  }

  public String getLocationPattern() {
    return locationPattern;
  }

  public void setLocationPattern(String locationPattern) {
    this.locationPattern = locationPattern;
  }

  public String getFileNamePattern() {
    return fileNamePattern;
  }

  public void setFileNamePattern(String fileNamePattern) {
    this.fileNamePattern = fileNamePattern;
  }
}
