package com.renan.refyne.features.user.dto.response;

import com.renan.refyne.features.user.enums.UserType;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserResponse {

  UUID publicId;
  String email;
  UserType userType;
  Boolean profileCompleted;
}
