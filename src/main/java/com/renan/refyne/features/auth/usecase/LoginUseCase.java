package com.renan.refyne.features.auth.usecase;

import com.renan.refyne.features.auth.dto.request.LoginRequest;
import com.renan.refyne.features.auth.dto.response.AuthResponse;
import com.renan.refyne.features.auth.service.IAuthenticationService;
import com.renan.refyne.features.auth.service.ILoginAttemptService;
import com.renan.refyne.features.auth.service.IRateLimitService;
import com.renan.refyne.features.auth.validator.LoginValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginUseCase {

  private final IRateLimitService rateLimitService;
  private final ILoginAttemptService loginAttemptService;
  private final LoginValidator loginValidator;
  private final IAuthenticationService authenticationService;

  public AuthResponse execute(
    LoginRequest request
  ) {

    if (!rateLimitService.tryConsume(request.getEmail())) {
      throw new RuntimeException("Too many requests");
    }

    try {
      loginValidator.validate(request);
      AuthResponse response = authenticationService.authenticate(request);

      loginAttemptService.loginSucceeded(request.getEmail());

      return response;

    } catch (Exception ex) {
      loginAttemptService.loginFailed(request.getEmail());

      throw ex;
    }
  }
}
