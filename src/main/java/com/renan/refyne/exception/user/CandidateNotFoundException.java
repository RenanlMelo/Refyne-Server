package com.renan.refyne.exception.user;

public class CandidateNotFoundException extends RuntimeException {
  public CandidateNotFoundException() {
    super("User not found");
  }
}
