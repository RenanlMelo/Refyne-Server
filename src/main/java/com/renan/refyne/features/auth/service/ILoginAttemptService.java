package com.renan.refyne.features.auth.service;

public interface ILoginAttemptService {

  void loginSucceeded(String email);

  void loginFailed(String email);

  boolean isBlocked(String email);
}
