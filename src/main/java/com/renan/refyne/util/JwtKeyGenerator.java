package com.renan.refyne.util;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class JwtKeyGenerator {

  public static void main(String[] args) {
    Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

    System.out.println("JWT Secret:");
    System.out.println(base64Key);
  }
}
