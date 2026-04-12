package com.renan.refyne.dto.Candidate;

import jakarta.validation.constraints.NotBlank;
import com.renan.refyne.enums.AvailabilityStatus;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class CandidateRequestDTO {
  @NotBlank
  private String fullName;

  private String headline;
  private String bio;
  private String city;
  private String state;
  private String country;
  private String profilePhoto;
  private String resumeUrl;
  private String linkedinUrl;
  private String portfolioUrl;
  private String githubUrl;
  private AvailabilityStatus availabilityStatus;

  @NotBlank
  @CPF
  private String cpf;
}
