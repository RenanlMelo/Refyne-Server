package com.renan.refyne.util;

import com.renan.refyne.dto.candidate.CandidateResponseDTO;
import com.renan.refyne.entity.Candidate;

public class CandidateMapper {

  public static CandidateResponseDTO toPartialCandidateDTO(Candidate candidate) {
    return CandidateResponseDTO.builder()
      .fullName(candidate.getFullName())
      .email(candidate.getUser().getEmail())
      .city(candidate.getCity())
      .state(candidate.getState())
      .country(candidate.getCountry())
      .resumeUrl(candidate.getResumeUrl())
      .linkedinUrl(candidate.getLinkedinUrl())
      .portfolioUrl(candidate.getPortfolioUrl())
      .githubUrl(candidate.getGithubUrl())
      .availabilityStatus(candidate.getAvailabilityStatus())
      // Intentional omissions to adhere to constraints:
      // .id(...) - Not in DTO
      // .cpf(...)
      // .headline(...)
      // .bio(...)
      .build();
  }
}
