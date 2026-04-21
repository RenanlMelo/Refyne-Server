package com.renan.refyne.repository;

import com.renan.refyne.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, String> {

  Optional<Skill> findByNomeNormalizado(String nomeNormalizado);

  // Search Skill
  @Query("""
        SELECT DISTINCT s FROM Skill s
        LEFT JOIN s.synonyms syn
        WHERE 
            LOWER(s.nomeNormalizado) LIKE LOWER(CONCAT('%', :term, '%'))
            OR LOWER(syn.synonym) LIKE LOWER(CONCAT('%', :term, '%'))
    """)
  List<Skill> searchSkills(@Param("term") String term);
}

