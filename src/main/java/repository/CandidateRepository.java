package com.renan.refyne;

import org.springframework.data.jpa.repository.JpaRepository;
import com.renan.refyne.entity.Candidate;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
  boolean existsByCpf(String cpf);
  Optional<Candidate> findByCpf(String cpf);
}

