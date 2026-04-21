package com.renan.refyne.entity;

import com.renan.refyne.enums.ApplicationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "APPLICATION",
  uniqueConstraints = @UniqueConstraint(columnNames = {"job_posting_id", "candidate_id"}))
public class Application {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer applicationId;

  @ManyToOne
  @JoinColumn(name = "job_posting_id", nullable = false)
  private JobPosting jobPosting;

  @ManyToOne
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;

  @Enumerated(EnumType.STRING)
  private ApplicationStatus status = ApplicationStatus.APPLIED;

  private LocalDateTime appliedAt;

  @PrePersist
  public void prePersist() {
    appliedAt = LocalDateTime.now();
  }
}
