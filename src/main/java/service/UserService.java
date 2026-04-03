package com.renan.refyne.service;

import com.renan.refyne.entity.User;
import com.renan.refyne.exception.auth.UserAlreadyInUseException;
import com.renan.refyne.exception.user.UserNotFoundException;
import com.renan.refyne.repository.UserRepository;
import dto.User.UserRequestDTO;
import dto.User.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
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

  public UserResponseDTO getUser(String email) {
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new UserNotFoundException());

    return toDTO(user);
  }

  private UserResponseDTO toDTO(User user) {
    return UserResponseDTO.builder()
      .id(user.getUserId())
      .email(user.getEmail())
      .userType(user.getUserType())
      .build();
  }
}
