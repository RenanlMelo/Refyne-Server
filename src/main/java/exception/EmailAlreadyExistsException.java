package com.renan.refyne.exception;

public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException() {
    super("Email already in use");
  }
}
