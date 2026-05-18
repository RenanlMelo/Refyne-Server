package com.renan.refyne.features.user.service;

public interface IRateLimitService {

  boolean tryConsume(String key);
}
