package com.renan.refyne.controller;

import com.renan.refyne.dto.candidate.PaginatedCandidateResponseDTO;
import com.renan.refyne.dto.jobPosting.JobPostingListDTO;
import com.renan.refyne.dto.jobPosting.JobPostingRequestDTO;
import com.renan.refyne.dto.jobPosting.JobPostingResponseDTO;
import com.renan.refyne.dto.jobPosting.JobSuggestionDTO;
import com.renan.refyne.entity.User;
import com.renan.refyne.enums.WorkModel;
import com.renan.refyne.service.ApplicationService;
import com.renan.refyne.service.JobPostingService;
import com.renan.refyne.util.SortParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {

  private final JobPostingService service;
  private final ApplicationService applicationService;

  public JobPostingController(JobPostingService service, ApplicationService applicationService) {
    this.service = service;
    this.applicationService = applicationService;
  }

  // CREATE
  @PostMapping("/create")
  public ResponseEntity<JobPostingResponseDTO> create(
    @RequestBody JobPostingRequestDTO dto,
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(service.createJob(dto, user));
  }
  // GET ALL
  @GetMapping
    public ResponseEntity<List<JobPostingListDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

  @GetMapping("/my-jobs")
  public ResponseEntity<List<JobPostingResponseDTO>> getMyJobs(
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(
      service.getJobsByStartup(user)
    );
  }

  @GetMapping("/search/suggestions")
  public ResponseEntity<List<JobSuggestionDTO>> suggestions(
    @RequestParam String q,
    @RequestParam(required = false) WorkModel workModel,
    @RequestParam(required = false) Double equityMin,
    @RequestParam(required = false) Double equityMax
  ) {
    return ResponseEntity.ok(
      service.getSuggestions(q, workModel, equityMin, equityMax)
    );
  }

  @GetMapping("/jobs/search")
  public ResponseEntity<Page<JobPostingResponseDTO>> searchJobs(
    @RequestParam(required = false) String query,
    @RequestParam(required = false) WorkModel workModel,
    @RequestParam(required = false) Double equityMin,
    @RequestParam(required = false) Double equityMax,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      service.searchJobs(query, workModel, equityMin, equityMax, page, size)
    );
  }

  @GetMapping("/{publicId}")
  public ResponseEntity<JobPostingResponseDTO> getByPublicId(
    @PathVariable UUID publicId
  ) {
    return ResponseEntity.ok(service.getByPublicId(publicId));
  }

  @GetMapping("/{publicId}/candidates")
  public ResponseEntity<PaginatedCandidateResponseDTO> getCandidatesByJob(
    @PathVariable UUID publicId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(required = false) String sort,
    @AuthenticationPrincipal User user
  ) {
    Pageable pageable = SortParser.toPageable(page, size, sort);
    return ResponseEntity.ok(
      applicationService.getCandidatesByJob(publicId, user, pageable)
    );
  }
}

