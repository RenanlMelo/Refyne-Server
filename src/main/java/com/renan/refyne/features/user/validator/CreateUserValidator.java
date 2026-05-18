package com.renan.refyne.features.user.validator;

import com.renan.refyne.features.user.dto.request.CreateUserRequest;

import com.renan.refyne.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserValidator {

  private final UserRepository userRepository;

  public void validate(
    CreateUserRequest request
  ) {

    boolean alreadyExists =
      userRepository
        .existsByEmailAndUserType(
          request.getEmail(),
          request.getUserType()
        );

    if (alreadyExists) {

      throw new RuntimeException(
        "Email already in use"
      );
    }
  }
}
