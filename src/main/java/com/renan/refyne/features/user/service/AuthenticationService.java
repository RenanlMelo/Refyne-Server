package com.renan.refyne.features.user.service;

import com.renan.refyne.features.auth.provider.JwtTokenProvider;
import com.renan.refyne.features.user.dto.request.LoginRequest;
import com.renan.refyne.features.user.dto.response.AuthResponse;
import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.mapper.UserMapper;
import com.renan.refyne.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final JwtTokenProvider tokenProvider;
  private final UserMapper userMapper;

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
      userRepository
        .findByEmailAndUserType(
          request.getEmail(),
          request.getUserType()
        )
        .orElseThrow(
          () -> new RuntimeException(
            "Invalid credentials"
          )
        );

    String token =
      tokenProvider.generateToken(user);

    return new AuthResponse(
      token,
      userMapper.toResponse(user)
    );
  }
}
