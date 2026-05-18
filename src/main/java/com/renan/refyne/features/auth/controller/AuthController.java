package com.renan.refyne.features.auth.controller;

import com.renan.refyne.features.auth.dto.request.LoginRequest;
import com.renan.refyne.features.auth.dto.request.RegisterRequest;
import com.renan.refyne.features.auth.dto.response.AuthResponse;
import com.renan.refyne.features.auth.usecase.LoginUseCase;
import com.renan.refyne.features.auth.usecase.RegisterUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final RegisterUseCase registerUseCase;
  private final LoginUseCase loginUseCase;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(
    @Valid @RequestBody RegisterRequest request
  ) {

    AuthResponse response =
      registerUseCase.execute(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
    @RequestBody LoginRequest request
  ) {

    return ResponseEntity.ok(
      loginUseCase.execute(request)
    );
  }
}
