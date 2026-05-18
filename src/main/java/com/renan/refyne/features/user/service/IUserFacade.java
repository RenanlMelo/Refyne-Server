package com.renan.refyne.features.user.service;

import com.renan.refyne.features.user.dto.response.UserResponse;
import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.enums.UserType;

public interface IUserFacade {

  User findByEmail(String email);

  User create(String email, String password, UserType userType);

  boolean existsByEmailAndUserType(String email, UserType userType);

  UserResponse toResponse(User user);
}
