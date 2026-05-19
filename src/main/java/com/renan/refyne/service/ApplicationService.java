package com.renan.refyne.service;

import com.renan.refyne.dto.application.ApplyRequestDTO;
import com.renan.refyne.dto.application.CandidateApplicationDTO;
import com.renan.refyne.dto.application.JobApplicationDetailDTO;
import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import com.renan.refyne.repository.ApplicationRepository;
import com.renan.refyne.repository.JobPostingRepository;
import com.renan.refyne.repository.StartupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import com.renan.refyne.entity.Candidate;
import com.renan.refyne.repository.CandidateRepository;
import com.renan.refyne.entity.Application;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

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

  public List<JobApplicationDetailDTO> getApplicationsByJob(
    UUID jobPublicId,
    User user
  ) {

    Startup startup = startupRepository.findByUser_UserId(user.getUserId())
      .orElseThrow(() ->
        new RuntimeException("Startup not found")
      );

    JobPosting job = jobPostingRepository.findByPublicId(jobPublicId)
      .orElseThrow(() ->
        new RuntimeException("Job not found")
      );

    if (!job.getStartup().getStartupId().equals(startup.getStartupId())) {
      throw new RuntimeException("You do not own this job posting");
    }

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
}
