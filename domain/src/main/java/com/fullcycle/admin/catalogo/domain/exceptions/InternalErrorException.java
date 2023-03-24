package com.fullcycle.admin.catalogo.domain.exceptions;

public class InternalErrorException extends NoStackTraceException{

  private InternalErrorException(String message) {
    super(message);
  }

  private InternalErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public static InternalErrorException with(String message, Throwable cause) {
    return new InternalErrorException(message, cause);
  }
}
