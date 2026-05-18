package com.renan.refyne.features.user.factory;

import com.renan.refyne.features.user.dto.request.CreateUserRequest;
import com.renan.refyne.features.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

  private final PasswordEncoder passwordEncoder;

  public UserFactory(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public User create(CreateUserRequest request) {

    User user = new User();

    user.setEmail(request.getEmail());

    user.setPasswordHash(
      passwordEncoder.encode(request.getPassword())
    );

    user.setUserType(request.getUserType());

    user.setProfileCompleted(false);

    return user;
  }
}
