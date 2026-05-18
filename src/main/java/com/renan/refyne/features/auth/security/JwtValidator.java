package com.renan.refyne.features.auth.security;

import com.renan.refyne.features.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtValidator {

  private final JwtClaimsExtractor claimsExtractor;

  public boolean isValid(
    String token,
    User user
  ) {

    String username =
      claimsExtractor.extractUsername(token);

    return username.equals(user.getEmail())
      && !isExpired(token);
  }

  public boolean isExpired(String token) {

    Date expiration =
      claimsExtractor.extractExpiration(token);

    return expiration.before(new Date());
  }
}
