package com.renan.refyne.repository;

import com.renan.refyne.entity.User;
import com.renan.refyne.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailAndUserType(String email, UserType userType);
  boolean existsByEmailAndUserType(String email, UserType userType);
  Optional<User> findByEmail(String email);
  Optional<User> findByUserId(Long userId);
}
