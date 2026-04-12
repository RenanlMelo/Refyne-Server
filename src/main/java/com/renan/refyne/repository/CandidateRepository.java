package com.renan.refyne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.renan.refyne.entity.Candidate;
import com.renan.refyne.entity.User;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

  boolean existsByCpf(String cpf);

  boolean existsByUser(User user);

  Optional<Candidate> findByCpf(String cpf);

  Optional<Candidate> findByUser(User user);
}

