package com.renan.refyne.controller;

import dto.User.UserRequestDTO;
import dto.User.UserResponseDTO;
import com.renan.refyne.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/create")
  public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
    UserResponseDTO user = userService.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @GetMapping("/{email}")
  public UserResponseDTO getUser(@PathVariable String email) {
    return userService.getUser(email);
  }
}
