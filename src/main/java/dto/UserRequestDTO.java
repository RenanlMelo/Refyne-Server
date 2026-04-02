package com.renan.refyne.dto;

import com.renan.refyne.enums.UserType;
import lombok.Data;

@Data
public class UserRequestDTO {
  private String email;
  private String password;
  private UserType userType;
}
