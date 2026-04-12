package com.renan.refyne.service;

import com.renan.refyne.entity.PasswordResetToken;
import com.renan.refyne.entity.User;
import com.renan.refyne.exception.auth.UserAlreadyInUseException;
import com.renan.refyne.repository.PasswordResetTokenRepository;
import com.renan.refyne.repository.UserRepository;
import com.renan.refyne.dto.User.UserRequestDTO;
import com.renan.refyne.dto.User.UserResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

  public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RateLimitService rateLimitService, LoginAttemptService loginAttemptService, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.rateLimitService = rateLimitService;
    this.loginAttemptService = loginAttemptService;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
    this.emailService = emailService;
  }

  public UserResponseDTO createUser(UserRequestDTO dto) {
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new UserAlreadyInUseException("Email");
    }

    User user = new User();
    user.setEmail(dto.getEmail());
    user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
    user.setUserType(dto.getUserType());

    User savedUser = userRepository.save(user);

    return toDTO(savedUser);
  }

  public User authenticateUser(UserRequestDTO dto) {
    if (!rateLimitService.tryConsume(dto.getEmail())) {
      throw new RuntimeException("Too many requests. Try again later.");
    }

    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          dto.getEmail(),
          dto.getPassword()
        )
      );

      loginAttemptService.loginSucceeded(dto.getEmail());

      return userRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    } catch (Exception e) {
      e.printStackTrace();
      loginAttemptService.loginFailed(dto.getEmail());
      throw new RuntimeException("Invalid credentials");
    }
  }

  public void forgotPassword(String email) {

    Optional<User> userOpt = userRepository.findByEmail(email);

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

  private UserResponseDTO toDTO(User user) {
    return UserResponseDTO.builder()
      .email(user.getEmail())
      .userType(user.getUserType())
      .build();
  }
}
