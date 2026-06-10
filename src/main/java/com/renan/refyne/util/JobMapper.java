package com.renan.refyne.util;

import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.dto.jobPosting.JobPostingResponseDTO;
import com.renan.refyne.dto.jobPosting.JobSuggestionDTO;

public class JobMapper {

  public static JobSuggestionDTO toSuggestionDTO(JobPosting job) {
    return JobSuggestionDTO.builder()
      .publicId(job.getPublicId())
      .title(job.getTitle())
      .startupName(job.getStartup().getCompanyName())
      .build();
  }

  public static JobPostingResponseDTO toDTO(JobPosting job) {
    return JobPostingResponseDTO.builder()
      .publicId(job.getPublicId())
      .title(job.getTitle())
      .description(job.getDescription())
      .startupName(job.getStartup().getCompanyName())
      .createdAt(job.getCreatedAt())
      .build();
  }
}
