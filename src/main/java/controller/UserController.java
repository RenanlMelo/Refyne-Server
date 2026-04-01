package com.renan.refyne.controller;

import com.renan.refyne.entity.User;
import com.renan.refyne.enums.UserType;
import com.renan.refyne.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping("/{create-user}")
  public User createUser(@RequestBody User request) {
    return service.createUser(
      request.getEmail(),
      request.getPasswordHash(),
      request.getUserType()
    );
  }

  @GetMapping("/{email}")
  public User getUser(@PathVariable String email) {
    return service.getUser(email);
  }
}
