package com.renan.refyne.controller;

import com.renan.refyne.dto.UserRequestDTO;
import com.renan.refyne.dto.UserResponseDTO;
import com.renan.refyne.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/create")
  public UserResponseDTO createUser(@RequestBody UserRequestDTO dto) {
    return userService.createUser(dto);
  }

  @GetMapping("/{email}")
  public UserResponseDTO getUser(@PathVariable String email) {
    return userService.getUser(email);
  }
}
