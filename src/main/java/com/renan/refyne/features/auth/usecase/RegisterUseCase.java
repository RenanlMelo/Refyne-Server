package com.renan.refyne.features.auth.usecase;

import com.renan.refyne.features.auth.dto.request.LoginRequest;
import com.renan.refyne.features.auth.dto.request.RegisterRequest;
import com.renan.refyne.features.auth.dto.response.AuthResponse;
import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.service.IUserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUseCase {

  private final IUserFacade userFacade;
  private final LoginUseCase loginUseCase;

  public AuthResponse execute(
    RegisterRequest request
  ) {

    User user = userFacade.create(
      request.getEmail(),
      request.getPassword(),
      request.getUserType()
    );

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(request.getEmail());
    loginRequest.setPassword(request.getPassword());
    loginRequest.setUserType(request.getUserType());

    return loginUseCase.execute(loginRequest);
  }
}
