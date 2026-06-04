package com.renan.refyne.dto.jobPosting;

import com.renan.refyne.entity.Skill;
import com.renan.refyne.enums.EmploymentType;
import com.renan.refyne.enums.JobStatus;
import com.renan.refyne.enums.WorkModel;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class JobPostingResponseDTO {

  UUID jobPostingId;
  UUID startupId;
  String startupName;

  String title;
  String description;
  String requirements;
  List<String> skills;
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
  Long applicationCount;
}
