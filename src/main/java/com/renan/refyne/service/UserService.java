package com.renan.refyne.service;

import com.renan.refyne.entity.PasswordResetToken;
import com.renan.refyne.entity.User;
import com.renan.refyne.enums.UserType;
import com.renan.refyne.exception.auth.UserAlreadyInUseException;
import com.renan.refyne.repository.PasswordResetTokenRepository;
import com.renan.refyne.repository.UserRepository;
import com.renan.refyne.dto.user.UserRequestDTO;
import com.renan.refyne.dto.user.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.renan.refyne.service.JwtService;
import com.renan.refyne.security.RateLimitService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final RateLimitService rateLimitService;
  private final LoginAttemptService loginAttemptService;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final EmailService emailService;
  private final JwtService jwtService;

  public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RateLimitService rateLimitService, LoginAttemptService loginAttemptService, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService, JwtService jwtService) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.rateLimitService = rateLimitService;
    this.loginAttemptService = loginAttemptService;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
    this.emailService = emailService;
    this.jwtService = jwtService;
  }

  public UserResponseDTO createUser(UserRequestDTO dto) {
    if (userRepository.existsByEmailAndUserType(dto.getEmail(), dto.getUserType())) {
      throw new UserAlreadyInUseException("Email");
    }

    User user = new User();
    user.setEmail(dto.getEmail());
    user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
    user.setUserType(dto.getUserType());

    User savedUser = userRepository.save(user);

    return buildAuthResponse(savedUser);
  }

  public UserResponseDTO authenticateUser(UserRequestDTO dto) {
    if (!rateLimitService.tryConsume(dto.getEmail())) {
      throw new RuntimeException("Too many requests. Try again later.");
    }

    try {

      User user = userRepository.findByEmailAndUserType(dto.getEmail(), dto.getUserType())
        .orElseThrow(() -> new RuntimeException("Invalid credentials"));

      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          dto.getEmail(),
          dto.getPassword()
        )
      );

      loginAttemptService.loginSucceeded(dto.getEmail());

      return buildAuthResponse(user);

    } catch (Exception e) {
      e.printStackTrace();
      loginAttemptService.loginFailed(dto.getEmail());
      throw new RuntimeException("Invalid credentials");
    }
  }

  public void forgotPassword(String email, UserType userType) {

    Optional<User> userOpt = userRepository.findByEmailAndUserType(email, userType);

    if (userOpt.isEmpty()) return;

    User user = userOpt.get();

    String token = UUID.randomUUID().toString();

    PasswordResetToken resetToken = new PasswordResetToken();
    resetToken.setToken(token);
    resetToken.setUser(user);
    resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

    passwordResetTokenRepository.save(resetToken);

    emailService.sendResetPasswordEmail(user.getEmail(), token);
  }

  public void resetPassword(String token, String newPassword) {

    PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
      .orElseThrow(() -> new RuntimeException("Invalid token"));

    if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Token expired");
    }

    User user = resetToken.getUser();

    user.setPasswordHash(passwordEncoder.encode(newPassword));

    userRepository.save(user);

    passwordResetTokenRepository.delete(resetToken);
  }

  private UserResponseDTO buildAuthResponse(User user) {
    String jwtToken = jwtService.generateToken(user);

    return UserResponseDTO.builder()
      .email(user.getEmail())
      .userType(user.getUserType())
      .token(jwtToken)
      .expiresIn(jwtService.getExpirationTime())
      .profileCompleted(user.isProfileCompleted())
      .build();
  }
}
