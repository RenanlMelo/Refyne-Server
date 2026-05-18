package com.renan.refyne.features.user.service;

import com.renan.refyne.features.user.dto.request.LoginRequest;
import com.renan.refyne.features.user.dto.response.AuthResponse;

public interface IAuthenticationService {

  AuthResponse authenticate(LoginRequest request);
}
