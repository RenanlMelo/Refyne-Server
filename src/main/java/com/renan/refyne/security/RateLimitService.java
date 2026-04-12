package com.renan.refyne.security;

import io.github.bucket4j.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  private Bucket createNewBucket() {
    return Bucket.builder()
      .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
      .build();
  }

  public boolean tryConsume(String key) {
    Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());
    return bucket.tryConsume(1);
  }
}
