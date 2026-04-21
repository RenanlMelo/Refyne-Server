package com.renan.refyne.entity;

import com.renan.refyne.enums.EmploymentType;
import com.renan.refyne.enums.JobStatus;
import com.renan.refyne.enums.WorkModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "JOB_POSTING")
public class JobPosting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long jobPostingId;

  @ManyToOne
  @JoinColumn(name = "startup_id", nullable = false)
  private Startup startup;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;

  @Column(columnDefinition = "TEXT")
  private String requirements;

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
    createdAt = LocalDateTime.now();
  }
}
