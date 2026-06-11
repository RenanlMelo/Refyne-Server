package com.renan.refyne.exception.global;

public class JobNotFoundException extends RuntimeException {
  public JobNotFoundException() {
    super("Job posting not found");
  }
}
