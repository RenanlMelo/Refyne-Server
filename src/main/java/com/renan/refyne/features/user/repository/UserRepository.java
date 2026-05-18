package com.renan.refyne.features.user.repository;

import com.renan.refyne.features.user.entity.User;
import com.renan.refyne.features.user.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmailAndUserType(String email, UserType userType);
  boolean existsByEmail(String email);
  Optional<User> findByEmailAndUserType(String email, UserType userType);
  Optional<User> findByEmail(String email);
  Optional<User> findByUserId(Long userId);
}
