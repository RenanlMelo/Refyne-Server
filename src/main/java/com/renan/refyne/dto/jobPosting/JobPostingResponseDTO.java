package com.renan.refyne.dto.jobPosting;

import com.renan.refyne.enums.EmploymentType;
import com.renan.refyne.enums.JobStatus;
import com.renan.refyne.enums.WorkModel;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class JobPostingResponseDTO {

  Long jobPostingId;
  Long startupId;
  String startupName;

  String title;
  String description;
  String requirements;
  EmploymentType employmentType;
  WorkModel workModel;

  String city;
  String state;
  String country;

  BigDecimal salaryMin;
  BigDecimal salaryMax;

  BigDecimal equityMin;
  BigDecimal equityMax;

  JobStatus jobStatus;
  LocalDateTime createdAt;
}
