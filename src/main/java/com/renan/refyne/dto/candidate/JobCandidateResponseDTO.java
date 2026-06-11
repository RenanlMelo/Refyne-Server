package com.renan.refyne.dto.candidate;

import com.renan.refyne.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class JobCandidateResponseDTO {

  String fullName;
  String email;
  String city;
  String state;
  String country;
  String resumeUrl;
  String linkedinUrl;
  String portfolioUrl;
  String githubUrl;
  AvailabilityStatus availabilityStatus;
}
