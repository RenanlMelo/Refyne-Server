package com.renan.refyne.exception.auth;

public class UserAlreadyInUseException extends RuntimeException {
  private final String info;

  public UserAlreadyInUseException(String info) {
    super(info + " already in use");
    this.info = info;
  }

  public String getError() {
    return info.toUpperCase() + "_ALREADY_EXISTS";
  }
}
