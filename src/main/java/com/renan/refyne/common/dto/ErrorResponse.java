package com.renan.refyne.common.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ErrorResponse {

  LocalDateTime timestamp;

  Integer status;

  String error;

  String message;
}
