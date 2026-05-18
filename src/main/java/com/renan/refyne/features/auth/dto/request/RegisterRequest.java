package com.renan.refyne.features.auth.dto.request;

import com.renan.refyne.features.user.enums.UserType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

@Value
@Data
public class RegisterRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must have at least 8 characters")
  @Pattern(
    regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).*$",
    message = "Password must contain at least one special character"
  )
  private String password;

  @NotNull(message = "User type is required")
  private UserType userType;
}
