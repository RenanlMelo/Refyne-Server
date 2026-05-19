package com.renan.refyne.dto.application;

import com.renan.refyne.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CandidateApplicationDTO(
  UUID applicationId,
  String jobTitle,
  String companyName,
  ApplicationStatus status,
  LocalDateTime appliedAt,
  UUID jobPublicId
) {}
