package com.renan.refyne.exception.global;

public class ForbiddenAccessException extends RuntimeException {
  public ForbiddenAccessException(String message) {
    super(message);
  }
}
