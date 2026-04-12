package com.renan.refyne.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

  private static final int MAX_ATTEMPTS = 3;
  private static final long BLOCK_TIME_MS = 20 * 60 * 1000; // 20 min

  private final Map<String, Integer> attempts = new HashMap<>();
  private final Map<String, Long> blockTime = new HashMap<>();

  public void loginFailed(String key) {
    int count = attempts.getOrDefault(key, 0) + 1;
    attempts.put(key, count);

    if (count >= MAX_ATTEMPTS) {
      blockTime.put(key, Instant.now().toEpochMilli());
    }
  }

  public void loginSucceeded(String key) {
    attempts.remove(key);
    blockTime.remove(key);
  }

  public boolean isBlocked(String key) {
    if (!blockTime.containsKey(key)) return false;

    long blockedAt = blockTime.get(key);

    if (Instant.now().toEpochMilli() - blockedAt > BLOCK_TIME_MS) {
      loginSucceeded(key);
      return false;
    }

    return true;
  }
}
