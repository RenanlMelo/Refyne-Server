package com.renan.refyne.entity;

import com.renan.refyne.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
  name = "application",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "UKl3badmiw3somwwciv1hcouoqj",
      columnNames = {"job_posting_id", "candidate_id"}
    )
  }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "application_id")
  private Long id;

  @Column(name = "public_id", nullable = false, unique = true)
  private UUID publicId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "candidate_id", nullable = false)
  private Candidate candidate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "job_posting_id", nullable = false)
  private JobPosting jobPosting;

  @Column(name = "cover_letter", columnDefinition = "varchar(max)")
  private String coverLetter;

  @Column(name = "resume_url", length = 255)
  private String resumeUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ApplicationStatus status;

  @Column(name = "applied_at", nullable = false)
  private LocalDateTime appliedAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.publicId = UUID.randomUUID();
    this.appliedAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.status = ApplicationStatus.APPLIED;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
