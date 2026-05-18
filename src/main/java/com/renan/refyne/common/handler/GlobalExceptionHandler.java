package com.renan.refyne.common.handler;

import com.renan.refyne.common.dto.ErrorResponse;
import com.renan.refyne.common.exception.EmailAlreadyExistsException;
import com.renan.refyne.common.exception.InvalidCredentialsException;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(
    EmailAlreadyExistsException.class
  )
  public ResponseEntity<ErrorResponse>
  handleEmailAlreadyExists(
    EmailAlreadyExistsException ex
  ) {

    ErrorResponse error =
      ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.CONFLICT.value())
        .error("Conflict")
        .message(ex.getMessage())
        .build();

    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(error);
  }

  @ExceptionHandler(
    InvalidCredentialsException.class
  )
  public ResponseEntity<ErrorResponse>
  handleInvalidCredentials(
    InvalidCredentialsException ex
  ) {

    ErrorResponse error =
      ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.UNAUTHORIZED.value())
        .error("Unauthorized")
        .message(ex.getMessage())
        .build();

    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(error);
  }
}
