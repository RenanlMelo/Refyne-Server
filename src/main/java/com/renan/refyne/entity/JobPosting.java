package com.renan.refyne.entity;

import com.renan.refyne.enums.EmploymentType;
import com.renan.refyne.enums.JobStatus;
import com.renan.refyne.enums.WorkModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
  name = "JOB_POSTING",
  indexes = {
    @Index(name = "idx_job_posting_public_id", columnList = "public_id")
  },
  uniqueConstraints = {
    @UniqueConstraint(columnNames = "public_id")
  })
public class JobPosting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long jobPostingId;

  @Column(name = "public_id", nullable = false, updatable = false, unique = true)
  private UUID publicId;

  @ManyToOne
  @JoinColumn(name = "startup_id", nullable = false)
  private Startup startup;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;

  @Column(columnDefinition = "TEXT")
  private String requirements;

  @ManyToMany
  @JoinTable(
    name = "job_posting_skills",
    joinColumns = @JoinColumn(name = "job_posting_id"),
    inverseJoinColumns = @JoinColumn(name = "skill_id")
  )
  private List<Skill> skills;

  @Enumerated(EnumType.STRING)
  private EmploymentType employmentType;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private WorkModel workModel;

  private String city;
  private String state;
  private String country;

  @Column(name = "salary_min", nullable = false, precision = 10, scale = 2)
  private BigDecimal salaryMin;
  @Column(name = "salary_max", nullable = false, precision = 10, scale = 2)
  private BigDecimal salaryMax;

  @Column(name = "equity_min", nullable = false, precision = 5, scale = 2)
  private BigDecimal equityMin;

  @Column(name = "equity_max", nullable = false, precision = 5, scale = 2)
  private BigDecimal equityMax;

  @Enumerated(EnumType.STRING)
  private JobStatus status = JobStatus.OPEN;

  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    if (this.publicId == null) {
      this.publicId = UUID.randomUUID();
    }

    if (this.createdAt == null) {
      this.createdAt = LocalDateTime.now();
    }
  }
}
