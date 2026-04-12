package com.renan.refyne.exception.auth;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid Credentials");
  }
}
