package dto.User;

import com.renan.refyne.enums.UserType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDTO {

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
