package com.renan.refyne.dto.user;

import com.renan.refyne.enums.UserType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthResponseDTO {
  String email;
  UserType userType;
  Boolean profileCompleted;
  String token;
}
