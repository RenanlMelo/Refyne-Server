package com.renan.refyne.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "FAVORITE_JOB",
  uniqueConstraints = @UniqueConstraint(columnNames = {"candidate_id", "job_posting_id"}))
public class FavoriteJob {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer favoriteJobId;

  @ManyToOne
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;

  @ManyToOne
  @JoinColumn(name = "job_posting_id", nullable = false)
  private JobPosting jobPosting;

  private LocalDateTime savedAt;

  @PrePersist
  public void prePersist() {
    savedAt = LocalDateTime.now();
  }
}
