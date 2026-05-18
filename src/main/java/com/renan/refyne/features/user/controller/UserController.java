package com.renan.refyne.features.user.controller;

import com.renan.refyne.features.user.dto.response.UserResponse;
import com.renan.refyne.features.user.usecase.GetUserProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final GetUserProfileUseCase getUserProfileUseCase;

  @GetMapping("/me")
  public ResponseEntity<UserResponse> me() {

    return ResponseEntity.ok(
      getUserProfileUseCase.execute()
    );
  }
}
