package com.renan.refyne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.renan.refyne.entity.Startup;

import java.util.Optional;

public interface StartupRepository extends JpaRepository<Startup, Integer> {
  boolean existsByCnpj(String cnpj);
  Optional<Startup> findByCnpj(String cnpj);
}
