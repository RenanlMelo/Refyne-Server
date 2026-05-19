package com.renan.refyne.dto.jobPosting;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class JobSuggestionDTO {
  UUID publicId;
  String title;
  String startupName;
}
