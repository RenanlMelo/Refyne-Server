package com.renan.refyne.features.auth.provider;

import com.renan.refyne.features.auth.config.JwtProperties;
import com.renan.refyne.features.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final JwtProperties properties;

  public String generateToken(User user) {

    Map<String, Object> claims = new HashMap<>();

    claims.put("userId", user.getPublicId());
    claims.put("role", user.getUserType().name());

    return Jwts.builder()
      .setClaims(claims)
      .setSubject(user.getEmail())
      .setIssuedAt(new Date())
      .setExpiration(
        new Date(
          System.currentTimeMillis()
            + properties.getExpirationTime()
        )
      )
      .signWith(
        getSigningKey(),
        SignatureAlgorithm.HS256
      )
      .compact();
  }

  public Key getSigningKey() {

    byte[] keyBytes = Decoders.BASE64.decode(
      properties.getSecretKey()
    );

    return Keys.hmacShaKeyFor(keyBytes);
  }
}
