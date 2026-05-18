package com.renan.refyne.common.exception;

public class InvalidCredentialsException
  extends RuntimeException {

  public InvalidCredentialsException() {
    super("Invalid credentials");
  }
}
