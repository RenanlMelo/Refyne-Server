package com.renan.refyne.dto.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class ForgotPasswordDTO {

  @NotBlank(message = "Email is required")
  private String email;
}
