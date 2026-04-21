package com.renan.refyne.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "FAVORITE_CANDIDATE",
  uniqueConstraints = @UniqueConstraint(columnNames = {"startup_id", "candidate_id", "job_posting_id"}))
public class FavoriteCandidate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer favoriteCandidateId;

  @ManyToOne
  @JoinColumn(name = "startup_id", nullable = false)
  private Startup startup;

  @ManyToOne
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;

  @ManyToOne
  @JoinColumn(name = "job_posting_id")
  private JobPosting jobPosting;

  private LocalDateTime savedAt;

  @Column(columnDefinition = "TEXT")
  private String notes;

  @PrePersist
  public void prePersist() {
    savedAt = LocalDateTime.now();
  }
}
