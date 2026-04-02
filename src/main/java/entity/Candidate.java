package com.renan.refyne.entity;

import com.renan.refyne.enums.AvailabilityStatus;
import com.renan.refyne.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.renan.refyne.enums.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
  name = "CANDIDATE",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = "user_id"),
    @UniqueConstraint(columnNames = "cpf")
  }
)
public class Candidate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "candidate_id")
  private Long candidateId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "full_name", nullable = false, length = 100)
  private String fullName;

  @Column(length = 200)
  private String headline;

  @Lob
  @Column
  private String bio;

  @Column(length = 50)
  private String city;

  @Column(length = 50)
  private String state;

  @Column(length = 50)
  private String country;

  @Column(name = "profile_photo", length = 200)
  private String profilePhoto;

  @Column(name = "resume_url", length = 200)
  private String resumeUrl;

  @Column(name = "linkedin_url", length = 200)
  private String linkedinUrl;

  @Column(name = "portfolio_url", length = 200)
  private String portfolioUrl;

  @Column(name = "github_url", length = 200)
  private String githubUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "availability_status", length = 20)
  private AvailabilityStatus availabilityStatus;

  @Column(name = "cpf", nullable = false, length = 50)
  private String cpf;
}
