package com.renan.refyne.dto.application;

import com.renan.refyne.enums.ApplicationStatus;
import com.renan.refyne.enums.AvailabilityStatus;
import com.renan.refyne.enums.WorkModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record JobApplicationDetailDTO(

  UUID applicationId,
  ApplicationStatus status,
  LocalDateTime appliedAt,
  String coverLetter,

  UUID candidateId,
  String fullName,
  String email,
  String phone,

  String headline,
  String bio,
  String resumeUrl,

  String city,
  String state,
  String country,

  WorkModel workModel,

  List<String> skills,

  String linkedinUrl,
  String portfolioUrl,
  String githubUrl,
  String profilePhoto,
  AvailabilityStatus availabilityStatus

) {
  /** Constructor used by JPQL projection (no phone, workModel, skills, or list fields) */
  public JobApplicationDetailDTO(
      UUID applicationId,
      ApplicationStatus status,
      LocalDateTime appliedAt,
      String coverLetter,
      UUID candidateId,
      String fullName,
      String email,
      String headline,
      String bio,
      String resumeUrl,
      String city,
      String state,
      String country,
      String linkedinUrl,
      String portfolioUrl,
      String githubUrl,
      String profilePhoto,
      AvailabilityStatus availabilityStatus
  ) {
      this(applicationId, status, appliedAt, coverLetter, candidateId, fullName, email,
           null, headline, bio, resumeUrl, city, state, country, null, null,
           linkedinUrl, portfolioUrl, githubUrl, profilePhoto, availabilityStatus);
  }
}
