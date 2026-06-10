package com.renan.refyne.dto.candidate;

import com.renan.refyne.enums.UserType;
import com.renan.refyne.enums.AvailabilityStatus;
import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonInclude;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateResponseDTO {

  String fullName;
  String email;
  String headline;
  String bio;
  String city;
  String state;
  String country;
  String profilePhoto;
  String resumeUrl;
  String linkedinUrl;
  String portfolioUrl;
  String githubUrl;
  AvailabilityStatus availabilityStatus;
  String cpf;
  UserType userType;
}
