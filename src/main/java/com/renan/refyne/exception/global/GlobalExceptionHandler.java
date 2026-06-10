package com.renan.refyne.exception.global;

import com.renan.refyne.exception.auth.UserAlreadyInUseException;
import com.renan.refyne.exception.auth.InvalidCredentialsException;
import com.renan.refyne.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.renan.refyne.dto.global.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Email/CPF/CNPJ DUPLICATED ACCOUNT HANDLER
  @ExceptionHandler(UserAlreadyInUseException.class)
  public ResponseEntity<ApiErrorResponse> handleUserExists(UserAlreadyInUseException ex) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(ApiErrorResponse.builder()
        .error(ex.getError())
        .message(ex.getMessage())
        .build());
  }

  // INVALID CREDENTIALS HANDLER
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(ApiErrorResponse.builder()
        .error("INVALID_CREDENTIALS")
        .message(ex.getMessage())
        .build());
  }

  // USER NOT FOUND HANDLER
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(ApiErrorResponse.builder()
        .error("USER_NOT_FOUND")
        .message(ex.getMessage())
        .build());
  }

  // RESOURCE NOT FOUND HANDLER
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(ApiErrorResponse.builder()
        .error("NOT_FOUND")
        .message(ex.getMessage())
        .build());
  }

  // FORBIDDEN ACCESS HANDLER
  @ExceptionHandler(ForbiddenAccessException.class)
  public ResponseEntity<ApiErrorResponse> handleForbiddenAccess(ForbiddenAccessException ex) {
    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(ApiErrorResponse.builder()
        .error("FORBIDDEN")
        .message(ex.getMessage())
        .build());
  }

  // INVALID ARGUMENT HANDLER
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ApiErrorResponse.builder()
        .error("BAD_REQUEST")
        .message(ex.getMessage())
        .build());
  }
}
