package com.renan.refyne.dto.jobPosting;

import lombok.Value;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Value
@AllArgsConstructor
public class JobPostingListDTO {

  UUID publicId;
  String title;
  String description;
  String companyName;
}
