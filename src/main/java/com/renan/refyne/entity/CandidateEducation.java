package com.renan.refyne.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "CANDIDATE_EDUCATION")
public class CandidateEducation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer candidateEducationId;

  @ManyToOne
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;

  private String institution;
  private String degree;
  private String fieldOfStudy;

  private LocalDate startDate;
  private LocalDate endDate;
}
