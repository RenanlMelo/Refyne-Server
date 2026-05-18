package com.renan.refyne.features.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService implements IPasswordService {

  private final PasswordEncoder encoder;

  public String hash(String password) {

    return encoder.encode(password);
  }

  public boolean matches(
    String raw,
    String encoded
  ) {

    return encoder.matches(raw, encoded);
  }
}
