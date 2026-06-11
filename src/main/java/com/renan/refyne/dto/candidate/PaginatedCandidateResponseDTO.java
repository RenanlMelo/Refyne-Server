package com.renan.refyne.dto.candidate;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PaginatedCandidateResponseDTO {

  List<JobCandidateResponseDTO> candidates;
  int page;
  int pageSize;
  long totalElements;
}
