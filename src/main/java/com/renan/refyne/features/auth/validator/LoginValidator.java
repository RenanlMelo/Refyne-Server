package com.renan.refyne.features.auth.validator;

import com.renan.refyne.features.auth.dto.request.LoginRequest;
import com.renan.refyne.features.user.service.ILoginAttemptService;
import com.renan.refyne.features.user.service.IUserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginValidator {

  private final IUserFacade userFacade;
  private final ILoginAttemptService loginAttemptService;

  public void validate(
    LoginRequest request
  ) {

    if (loginAttemptService.isBlocked(request.getEmail())) {
      throw new RuntimeException("Account temporarily blocked");
    }

    boolean exists = userFacade
      .existsByEmailAndUserType(
        request.getEmail(),
        request.getUserType()
      );

    if (!exists) {
      throw new RuntimeException("Invalid credentials");
    }
  }
}
