package com.renan.refyne.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "CANDIDATE_EXPERIENCE")
public class CandidateExperience {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer experienceId;

  @ManyToOne
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;

  private String company;
  private String title;

  private LocalDate startDate;
  private LocalDate endDate;

  private Boolean isCurrentJob = false;

  @Column(columnDefinition = "TEXT")
  private String description;
}
