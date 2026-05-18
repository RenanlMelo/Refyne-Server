package com.renan.refyne.features.user.usecase;

import com.renan.refyne.features.user.dto.response.UserResponse;
import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.mapper.UserMapper;
import com.renan.refyne.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserProfileUseCase {

  private final UserRepository userRepository;

  public UserResponse execute() {

    User authenticatedUser =
      (User) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();

    User user = userRepository
      .findById(authenticatedUser.getUserId())
      .orElseThrow();

    return UserMapper.toResponse(user);
  }
}
