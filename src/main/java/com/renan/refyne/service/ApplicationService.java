package com.renan.refyne.service;

import com.renan.refyne.dto.application.ApplyRequestDTO;
import com.renan.refyne.dto.application.CandidateApplicationDTO;
import com.renan.refyne.dto.application.JobApplicationDetailDTO;
import com.renan.refyne.dto.candidate.JobCandidateResponseDTO;
import com.renan.refyne.dto.candidate.PaginatedCandidateResponseDTO;
import com.renan.refyne.entity.Application;
import com.renan.refyne.entity.Candidate;
import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import com.renan.refyne.exception.global.JobNotFoundException;
import com.renan.refyne.exception.global.UnauthorizedAccessException;
import com.renan.refyne.repository.ApplicationRepository;
import com.renan.refyne.repository.CandidateRepository;
import com.renan.refyne.repository.JobPostingRepository;
import com.renan.refyne.repository.StartupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService {

  private final ApplicationRepository applicationRepository;
  private final JobPostingRepository jobPostingRepository;
  private final StartupRepository startupRepository;
  private final CandidateRepository candidateRepository;
  private final FileUploadService fileUploadService;

  public ApplicationService(
    ApplicationRepository applicationRepository,
    JobPostingRepository jobPostingRepository,
    StartupRepository startupRepository,
    CandidateRepository candidateRepository,
    FileUploadService fileUploadService
  ) {
    this.applicationRepository = applicationRepository;
    this.jobPostingRepository = jobPostingRepository;
    this.startupRepository = startupRepository;
    this.candidateRepository = candidateRepository;
    this.fileUploadService = fileUploadService;
  }

  public List<JobApplicationDetailDTO> getApplicationsByJob(UUID jobPublicId, User user) {
    assertStartupOwnsJob(user, jobPublicId);
    return applicationRepository.findApplicationsByJob(jobPublicId);
  }

  public List<CandidateApplicationDTO> getMyApplications(User user) {
    Candidate candidate = candidateRepository.findByUser_UserId(user.getUserId())
      .orElseThrow(() -> new RuntimeException("Candidate not found"));
    return applicationRepository.findApplicationsByCandidate(candidate.getPublicId());
  }

  public Application apply(ApplyRequestDTO dto, MultipartFile resume, User user) {
    Candidate candidate = candidateRepository.findByUser_UserId(user.getUserId())
      .orElseThrow(() -> new RuntimeException("Candidate not found"));

    JobPosting job = jobPostingRepository.findByPublicId(dto.jobPublicId())
      .orElseThrow(() -> new RuntimeException("Job not found"));

    boolean alreadyApplied = applicationRepository.existsByJobPosting_PublicIdAndCandidate_PublicId(
      dto.jobPublicId(), candidate.getPublicId()
    );

    if (alreadyApplied) {
      throw new RuntimeException("You have already applied for this job");
    }

    String resumeUrl = dto.resumeUrl();
    if (resume != null && !resume.isEmpty()) {
      try {
        resumeUrl = fileUploadService.uploadCandidateResume(resume);
      } catch (IOException e) {
        throw new RuntimeException("Error uploading resume", e);
      }
    }

    Application application = Application.builder()
      .candidate(candidate)
      .jobPosting(job)
      .coverLetter(dto.coverLetter())
      .resumeUrl(resumeUrl)
      .build();

    return applicationRepository.save(application);
  }

  public PaginatedCandidateResponseDTO getCandidatesByJob(UUID jobPublicId, User user, Pageable pageable) {
    assertStartupOwnsJob(user, jobPublicId);

    Page<JobCandidateResponseDTO> result =
      applicationRepository.findCandidatesByJobPublicId(jobPublicId, pageable);

    return PaginatedCandidateResponseDTO.builder()
      .candidates(result.getContent())
      .page(result.getNumber())
      .pageSize(result.getSize())
      .totalElements(result.getTotalElements())
      .build();
  }

  // ---------------------------------------------------------------------------
  // Private helpers
  // ---------------------------------------------------------------------------

  /**
   * Verifies that the authenticated {@code user} is a startup owner and that
   * the startup owns the job identified by {@code jobPublicId}.
   * Throws {@link UnauthorizedAccessException} or {@link JobNotFoundException}
   * on any violation.
   */
  private void assertStartupOwnsJob(User user, UUID jobPublicId) {
    Startup startup = startupRepository.findByUser_UserId(user.getUserId())
      .orElseThrow(() -> new UnauthorizedAccessException("User does not have a startup profile"));

    JobPosting job = jobPostingRepository.findByPublicId(jobPublicId)
      .orElseThrow(JobNotFoundException::new);

    if (!job.getStartup().getStartupId().equals(startup.getStartupId())) {
      throw new UnauthorizedAccessException("You do not own this job posting");
    }
  }
}
