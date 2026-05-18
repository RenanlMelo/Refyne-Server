package com.renan.refyne.features.auth.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import org.springframework.stereotype.Service;

import java.time.Duration;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService implements IRateLimitService {

  private final Map<String, Bucket> buckets =
    new ConcurrentHashMap<>();

  public boolean tryConsume(
    String key
  ) {

    Bucket bucket =
      buckets.computeIfAbsent(
        key,
        this::createBucket
      );

    return bucket.tryConsume(1);
  }

  private Bucket createBucket(
    String key
  ) {

    Bandwidth limit =
      Bandwidth.classic(
        5,
        Refill.intervally(
          5,
          Duration.ofMinutes(1)
        )
      );

    return Bucket.builder()
      .addLimit(limit)
      .build();
  }
}
