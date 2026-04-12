package com.renan.refyne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import java.util.Optional;

public interface StartupRepository extends JpaRepository<Startup, Long> {

  boolean existsByCnpj(String cnpj);

  boolean existsByUser(User user);

  Optional<Startup> findByCnpj(String cnpj);

  Optional<Startup> findByUser(User user);
}
