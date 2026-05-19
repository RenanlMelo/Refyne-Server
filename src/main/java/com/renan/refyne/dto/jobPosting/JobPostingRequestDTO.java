package com.renan.refyne.dto.jobPosting;

import com.renan.refyne.enums.JobStatus;
import com.renan.refyne.enums.EmploymentType;
import com.renan.refyne.enums.WorkModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JobPostingRequestDTO {

  private String title;
  private String description;
  private String requirements;
  private List<Long> skillIds;

  private EmploymentType employmentType;
  private WorkModel workModel;

  private String city;
  private String state;
  private String country;

  private BigDecimal salaryMin;
  private BigDecimal salaryMax;

  private BigDecimal equityMin;
  private BigDecimal equityMax;

  private JobStatus status;
}
