package com.renan.refyne.features.user.validator;
import com.renan.refyne.features.user.dto.request.LoginRequest;
import com.renan.refyne.features.user.repository.UserRepository;
import com.renan.refyne.features.user.service.ILoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginValidator {

  private final UserRepository userRepository;
  private final ILoginAttemptService loginAttemptService;

  public void validate(
    LoginRequest request
  ) {

    if (loginAttemptService.isBlocked(request.getEmail())) {
      throw new RuntimeException("Account temporarily blocked");
    }

    boolean exists = userRepository
        .existsByEmailAndUserType(
          request.getEmail(),
          request.getUserType()
        );

    if (!exists) {
      throw new RuntimeException("Invalid credentials");
    }
  }
}
