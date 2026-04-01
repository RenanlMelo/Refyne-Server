package com.renan.refyne.service;

import com.renan.refyne.entity.User;
import com.renan.refyne.enums.UserType;
import com.renan.refyne.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public User createUser(String email, String passwordHash, UserType userType) {
    User user = new User();
    user.setEmail(email);
    user.setPasswordHash(passwordHash);
    user.setUserType(userType);

    return repository.save(user);
  }

  public User getUser(String email) {
    return repository.findByEmail(email)
      .orElseThrow(() -> new RuntimeException("User not found"));
  }
}
