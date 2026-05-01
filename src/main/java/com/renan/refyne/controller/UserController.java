package com.renan.refyne.controller;

import com.renan.refyne.dto.user.*;
import com.renan.refyne.entity.User;
import com.renan.refyne.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.renan.refyne.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;

  public UserController(UserService userService, JwtService jwtService) {

    this.userService = userService;
    this.jwtService = jwtService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> getMe(
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(userService.getCurrentUser(user.getUserId()));
  }

  @PostMapping("/create")
  public ResponseEntity<AuthResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
    AuthResponseDTO response = userService.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody UserRequestDTO dto) {
    AuthResponseDTO response = userService.authenticateUser(dto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/forgot-password")
  public void forgotPassword(@RequestBody ForgotPasswordDTO dto) {userService.forgotPassword(dto.getEmail(), dto.getUserType());
  }

  @PostMapping("/reset-password")
  public void resetPassword(@RequestBody ResetPasswordDTO dto) {
    userService.resetPassword(dto.getToken(), dto.getNewPassword());
  }
}
