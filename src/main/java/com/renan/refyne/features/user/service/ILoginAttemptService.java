package com.renan.refyne.features.user.service;

public interface ILoginAttemptService {

  void loginSucceeded(String email);

  void loginFailed(String email);

  boolean isBlocked(String email);
}
