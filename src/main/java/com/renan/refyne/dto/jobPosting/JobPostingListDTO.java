package com.renan.refyne.dto.jobPosting;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class JobPostingListDTO {
    Integer jobPostingId;
    String title;
    String description;
    String companyName;
}