package com.renan.refyne.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CANDIDATE_SKILL")
public class CandidateSkill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer candidateSkillId;

  @ManyToOne
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;

  private String skillName;
  private String proficiencyLevel;
}
