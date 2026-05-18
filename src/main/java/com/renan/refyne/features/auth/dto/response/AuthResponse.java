package com.renan.refyne.features.auth.dto.response;

import com.renan.refyne.features.user.dto.response.UserResponse;

public record AuthResponse(
  String token,
  UserResponse user
) {
}
