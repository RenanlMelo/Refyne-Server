package com.renan.refyne.dto.jobPosting;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JobSuggestionDTO {
  Long id;
  String title;
  String startupName;
}
