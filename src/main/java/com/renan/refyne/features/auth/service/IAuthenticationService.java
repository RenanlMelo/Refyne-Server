package com.renan.refyne.features.auth.service;

import com.renan.refyne.features.auth.dto.request.LoginRequest;
import com.renan.refyne.features.auth.dto.response.AuthResponse;

public interface IAuthenticationService {

  AuthResponse authenticate(LoginRequest request);
}
