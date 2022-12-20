package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {NotFoundException.class})
  public ResponseEntity<?> handleNotFoundException(
      final DomainException exception
  ) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(exception));
  }

  @ExceptionHandler(value = {DomainException.class})
  public ResponseEntity<?> handleDomainException(
      final DomainException exception
  ) {

    return ResponseEntity.unprocessableEntity().body(ApiError.from(exception));
  }

  record ApiError(String message, List<Error> errors) {

    public static ApiError from(final DomainException ex) {
      return new ApiError(ex.getMessage(), ex.getErrors());
    }
  }

}
