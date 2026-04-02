package com.renan.refyne.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.renan.refyne.exception.EmailAlreadyExistsException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<Object> handleEmailExists(EmailAlreadyExistsException ex) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(Map.of(
        "error", "EMAIL_ALREADY_EXISTS",
        "message", ex.getMessage()
      ));
  }
}
