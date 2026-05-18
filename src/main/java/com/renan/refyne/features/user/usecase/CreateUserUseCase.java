package com.renan.refyne.features.user.usecase;

import com.renan.refyne.features.user.dto.request.CreateUserRequest;
import com.renan.refyne.features.user.dto.response.UserResponse;
import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.factory.UserFactory;
import com.renan.refyne.features.user.mapper.UserMapper;
import com.renan.refyne.features.user.repository.UserRepository;
import com.renan.refyne.features.user.validator.CreateUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UserRepository userRepository;
  private final CreateUserValidator validator;
  private final UserFactory userFactory;
  private final UserMapper userMapper;

  public UserResponse execute(
    CreateUserRequest request
  ) {

    validator.validate(request);

    User user =
      userFactory.create(request);

    User savedUser =
      userRepository.save(user);

    return userMapper.toResponse(savedUser);
  }
}
