package com.renan.refyne.controller;

import com.renan.refyne.dto.application.ApplyRequestDTO;
import com.renan.refyne.dto.application.CandidateApplicationDTO;
import com.renan.refyne.dto.application.JobApplicationDetailDTO;
import com.renan.refyne.entity.User;
import com.renan.refyne.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

  private final ApplicationService service;

  public ApplicationController(ApplicationService service) {
    this.service = service;
  }

  @GetMapping("/job/{jobPublicId}")
  public ResponseEntity<List<JobApplicationDetailDTO>> getApplicationsByJob(
    @PathVariable UUID jobPublicId,
    @AuthenticationPrincipal User user
  ) {

    return ResponseEntity.ok(
      service.getApplicationsByJob(jobPublicId, user)
    );
  }

  @GetMapping("/me")
  public ResponseEntity<List<CandidateApplicationDTO>> getMyApplications(
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(service.getMyApplications(user));
  }

  @PostMapping("/apply")
  public ResponseEntity<Void> apply(
    @RequestPart("data") ApplyRequestDTO dto,
    @RequestPart(value = "resume", required = false) MultipartFile resume,
    @AuthenticationPrincipal User user
  ) {
    service.apply(dto, resume, user);
    return ResponseEntity.ok().build();
  }
}
