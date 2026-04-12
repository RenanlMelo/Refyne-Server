package com.renan.refyne.controller;

import com.renan.refyne.dto.User.ForgotPasswordDTO;
import com.renan.refyne.dto.User.ResetPasswordDTO;
import com.renan.refyne.dto.User.UserRequestDTO;
import com.renan.refyne.dto.User.UserResponseDTO;
import com.renan.refyne.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.renan.refyne.service.JwtService;
import com.renan.refyne.entity.User;

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
    UserResponseDTO user = userService.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponseDTO> authenticateUser(@RequestBody UserRequestDTO dto) {

    User user = userService.authenticateUser(dto);

    String jwtToken = jwtService.generateToken(user);

    UserResponseDTO response = UserResponseDTO.builder()
      .email(user.getEmail())
      .userType(user.getUserType())
      .token(jwtToken)
      .expiresIn(jwtService.getExpirationTime())
      .build();

    return ResponseEntity.ok(response);
  }

  @PostMapping("/forgot-password")
  public void forgotPassword(@RequestBody ForgotPasswordDTO dto) {userService.forgotPassword(dto.getEmail());
  }

  @PostMapping("/reset-password")
  public void resetPassword(@RequestBody ResetPasswordDTO dto) {
    userService.resetPassword(dto.getToken(), dto.getNewPassword());
  }
}
