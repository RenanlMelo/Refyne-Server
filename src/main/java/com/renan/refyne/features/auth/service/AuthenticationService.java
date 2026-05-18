package com.renan.refyne.features.auth.service;

import com.renan.refyne.features.auth.dto.request.LoginRequest;
import com.renan.refyne.features.auth.dto.response.AuthResponse;
import com.renan.refyne.features.auth.provider.JwtTokenProvider;
import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.service.IUserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final IUserFacade userFacade;
  private final JwtTokenProvider tokenProvider;

  public AuthResponse authenticate(
    LoginRequest request
  ) {

    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
      )
    );

    User user =
      userFacade.findByEmail(request.getEmail());

    if (user == null || !user.getUserType().equals(request.getUserType())) {
      throw new RuntimeException("Invalid credentials");
    }

    String token =
      tokenProvider.generateToken(user);

    return new AuthResponse(
      token,
      userFacade.toResponse(user)
    );
  }
}
