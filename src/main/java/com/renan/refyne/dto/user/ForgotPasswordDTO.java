package com.renan.refyne.dto.user;

import com.renan.refyne.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordDTO {

  @NotBlank(message = "Email is required")
  private String email;
  private UserType userType;
}
