package com.fullcycle.admin.catalogo.domain.utils;

import java.util.UUID;

public final class IdUtils {

  private IdUtils() {
  }

  public static String generate() {
    return UUID.randomUUID().toString();
  }

}
