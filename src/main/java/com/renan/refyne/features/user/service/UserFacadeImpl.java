package com.renan.refyne.features.user.service;

import com.renan.refyne.features.user.dto.request.CreateUserRequest;
import com.renan.refyne.features.user.dto.response.UserResponse;
import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.enums.UserType;
import com.renan.refyne.features.user.factory.UserFactory;
import com.renan.refyne.features.user.mapper.UserMapper;
import com.renan.refyne.features.user.repository.UserRepository;
import com.renan.refyne.features.user.validator.CreateUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements IUserFacade {

  private final UserRepository userRepository;
  private final UserFactory userFactory;
  private final CreateUserValidator validator;
  private final UserMapper userMapper;

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  @Override
  public User create(String email, String password, UserType userType) {
    CreateUserRequest request = new CreateUserRequest(email, password, userType);

    validator.validate(request);

    User user = userFactory.create(request);
    return userRepository.save(user);
  }

  @Override
  public boolean existsByEmailAndUserType(String email, UserType userType) {
    return userRepository.existsByEmailAndUserType(email, userType);
  }

  @Override
  public UserResponse toResponse(User user) {
    return userMapper.toResponse(user);
  }
}
