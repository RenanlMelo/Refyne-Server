package com.renan.refyne.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDTO {

  private String token;

  @NotBlank(message = "Password is required")
  private String newPassword;
}
