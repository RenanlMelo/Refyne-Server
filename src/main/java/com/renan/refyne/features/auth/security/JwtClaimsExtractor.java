package com.renan.refyne.features.auth.security;

import com.renan.refyne.features.auth.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtClaimsExtractor {

  private final JwtTokenProvider tokenProvider;

  public String extractUsername(String token) {

    return extractClaim(
      token,
      Claims::getSubject
    );
  }

  public UUID extractUserId(String token) {

    return UUID.fromString(
      extractClaim(token, claims ->
        claims.get("userId", String.class)
      )
    );
  }

  public Date extractExpiration(String token) {

    return extractClaim(
      token,
      Claims::getExpiration
    );
  }

  public <T> T extractClaim(
    String token,
    Function<Claims, T> resolver
  ) {

    Claims claims = extractAllClaims(token);

    return resolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {

    return Jwts.parserBuilder()
      .setSigningKey(
        tokenProvider.getSigningKey()
      )
      .build()
      .parseClaimsJws(token)
      .getBody();
  }
}
