package com.renan.refyne.features.auth.service;

public interface IRateLimitService {

  boolean tryConsume(String key);
}
