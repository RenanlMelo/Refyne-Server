package com.renan.refyne.controller;

import com.renan.refyne.dto.jobPosting.JobPostingRequestDTO;
import com.renan.refyne.dto.jobPosting.JobPostingResponseDTO;
import com.renan.refyne.entity.User;
import com.renan.refyne.service.JobPostingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {

  private final JobPostingService service;

  public JobPostingController(JobPostingService service) {
    this.service = service;
  }

  // CREATE
  @PostMapping("/create")
  public ResponseEntity<JobPostingResponseDTO> create(
    @RequestBody JobPostingRequestDTO dto,
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(service.create(dto, user));
  }
  // GET ALL
  @GetMapping
  public ResponseEntity<List<JobPostingResponseDTO>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  // GET BY ID
  @GetMapping("/{id}")
  public ResponseEntity<JobPostingResponseDTO> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.getById(id));
  }

  // UPDATE
  @PutMapping("/{id}")
  public ResponseEntity<JobPostingResponseDTO> update(
    @PathVariable Integer id,
    @RequestBody JobPostingRequestDTO dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/my-jobs")
  public ResponseEntity<List<JobPostingResponseDTO>> getMyJobs(
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(service.getJobsByStartup(user));
  }
}
