package com.renan.refyne.features.auth.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService implements ILoginAttemptService {

  private static final int MAX_ATTEMPTS = 5;

  private final Map<String, Integer> attempts =
    new ConcurrentHashMap<>();

  public void loginSucceeded(
    String email
  ) {

    attempts.remove(email);
  }

  public void loginFailed(
    String email
  ) {

    attempts.merge(
      email,
      1,
      Integer::sum
    );
  }

  public boolean isBlocked(
    String email
  ) {

    return attempts.getOrDefault(
      email,
      0
    ) >= MAX_ATTEMPTS;
  }
}
