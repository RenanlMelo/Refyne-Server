package com.renan.refyne.controller;

import com.renan.refyne.dto.user.ForgotPasswordDTO;
import com.renan.refyne.dto.user.ResetPasswordDTO;
import com.renan.refyne.dto.user.UserRequestDTO;
import com.renan.refyne.dto.user.UserResponseDTO;
import com.renan.refyne.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @PostMapping("/create")
  public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
    UserResponseDTO response = userService.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponseDTO> authenticateUser(@RequestBody UserRequestDTO dto) {
    UserResponseDTO response = userService.authenticateUser(dto);
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
