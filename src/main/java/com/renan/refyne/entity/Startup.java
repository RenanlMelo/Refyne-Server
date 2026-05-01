package com.renan.refyne.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
  name = "STARTUP",
  indexes = {
    @Index(name = "idx_startup_user", columnList = "user_id"),
    @Index(name = "idx_startup_public_id", columnList = "public_id")
  },
  uniqueConstraints = {
    @UniqueConstraint(columnNames = "user_id"),
    @UniqueConstraint(columnNames = "cnpj"),
    @UniqueConstraint(columnNames = "public_id")
  }
)
public class Startup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "startup_id")
  private Long startupId;

  @Column(name = "public_id", nullable = false, updatable = false, unique = true, length = 36)
  private String publicId;

  @PrePersist
  public void generatePublicId() {
    this.publicId = UUID.randomUUID().toString();
  }

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "company_name", nullable = false, length = 100)
  private String companyName;

  @Lob
  @Column
  private String description;

  @Column(length = 50)
  private String industry;

  @Column(length = 50)
  private String stage;

  @Column(name = "founded_date")
  private LocalDate foundedDate;

  @Column(length = 50)
  private String size;

  @Column(name = "logo_url", length = 200)
  private String logoUrl;

  @Column(name = "website_url", length = 200)
  private String websiteUrl;

  @Column(name = "linkedin_url", length = 200)
  private String linkedinUrl;

  @Column(length = 50)
  private String city;

  @Column(length = 50)
  private String state;

  @Column(length = 50)
  private String country;

  @Column(name = "cnpj", nullable = false, length = 50)
  private String cnpj;
}
