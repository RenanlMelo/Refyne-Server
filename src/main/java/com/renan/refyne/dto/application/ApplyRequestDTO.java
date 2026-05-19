package com.renan.refyne.dto.application;

import java.util.UUID;

public record ApplyRequestDTO(
  UUID jobPublicId,
  String coverLetter,
  String resumeUrl
) {}
