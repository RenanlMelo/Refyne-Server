package com.renan.refyne.features.user.mapper;

import com.renan.refyne.features.user.dto.response.UserResponse;
import com.renan.refyne.features.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public static UserResponse toResponse(User user) {

    return UserResponse.builder()
      .publicId(user.getPublicId())
      .email(user.getEmail())
      .userType(user.getUserType())
      .profileCompleted(user.isProfileCompleted())
      .build();
  }
}
