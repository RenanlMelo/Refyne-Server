package com.renan.refyne.dto.User;

import com.renan.refyne.enums.UserType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponseDTO {

  Long id;
  String email;
  UserType userType;

  String token;
  Long expiresIn;
}
