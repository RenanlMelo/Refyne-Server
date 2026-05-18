package com.renan.refyne.features.user.dto.response;

public record AuthResponse(
  String token,
  UserResponse user
) {
}
