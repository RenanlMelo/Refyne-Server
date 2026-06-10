package com.renan.refyne.service;

import com.renan.refyne.dto.jobPosting.JobPostingListDTO;
import com.renan.refyne.dto.jobPosting.JobPostingRequestDTO;
import com.renan.refyne.dto.jobPosting.JobPostingResponseDTO;
import com.renan.refyne.dto.jobPosting.JobSuggestionDTO;
import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.entity.Skill;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import com.renan.refyne.enums.WorkModel;
import com.renan.refyne.repository.ApplicationRepository;
import com.renan.refyne.repository.JobPostingRepository;
import com.renan.refyne.repository.SkillRepository;
import com.renan.refyne.repository.StartupRepository;
import com.renan.refyne.util.JobMapper;
import com.renan.refyne.util.CandidateMapper;
import com.renan.refyne.dto.candidate.CandidateResponseDTO;
import com.renan.refyne.dto.global.PaginatedResponseDTO;
import com.renan.refyne.entity.Candidate;
import com.renan.refyne.entity.Application;
import com.renan.refyne.exception.global.ResourceNotFoundException;
import com.renan.refyne.exception.global.ForbiddenAccessException;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobPostingService {

  private final JobPostingRepository jobPostingRepository;
  private final StartupRepository startupRepository;
  private final SkillRepository skillRepository;
  private final ApplicationRepository applicationRepository;

  public JobPostingService(
    JobPostingRepository jobPostingRepository,
    StartupRepository startupRepository,
    SkillRepository skillRepository,
    ApplicationRepository applicationRepository
  ) {
    this.jobPostingRepository = jobPostingRepository;
    this.startupRepository = startupRepository;
    this.skillRepository = skillRepository;
    this.applicationRepository = applicationRepository;
  }

  public List<JobPostingListDTO> getAll() {

    return jobPostingRepository.findAllJobsDTO();
  }

  public JobPostingResponseDTO getByPublicId(UUID publicId) {

    JobPosting job = jobPostingRepository.findByPublicId(publicId)
      .orElseThrow(() -> new RuntimeException("Job not found"));

    return toDTO(job);
  }

  public List<JobPostingResponseDTO> getJobsByStartup(User user) {

    Startup startup = startupRepository.findByUser(user)
      .orElseThrow(() -> new RuntimeException("Startup not found for user"));

    return jobPostingRepository
      .findByStartupPublicId(startup.getPublicId())
      .stream()
      .map(this::toDTO)
      .toList();
  }

  public JobPostingResponseDTO createJob(JobPostingRequestDTO dto, User user) {

    if (dto.getSalaryMin().compareTo(dto.getSalaryMax()) > 0) {
      throw new RuntimeException("Salary min cannot be greater than max");
    }

    if (dto.getEquityMin() == null || dto.getEquityMax() == null) {
      throw new RuntimeException("Equity range is required");
    }

    if (dto.getEquityMin().compareTo(dto.getEquityMax()) > 0) {
      throw new RuntimeException("Equity min cannot be greater than max");
    }

    Startup startup = startupRepository.findByUser(user)
      .orElseThrow(() -> new RuntimeException("User does not have a startup"));

    JobPosting job = new JobPosting();
    job.setStartup(startup);

    job.setTitle(dto.getTitle());
    job.setDescription(dto.getDescription());
    job.setRequirements(dto.getRequirements());
    job.setEmploymentType(dto.getEmploymentType());
    job.setWorkModel(dto.getWorkModel());

    job.setCity(dto.getCity());
    job.setState(dto.getState());
    job.setCountry(dto.getCountry());

    job.setSalaryMin(dto.getSalaryMin());
    job.setSalaryMax(dto.getSalaryMax());
    job.setEquityMin(dto.getEquityMin());
    job.setEquityMax(dto.getEquityMax());

    List<Skill> skills = skillRepository.findAllById(dto.getSkillIds());

    if (skills.size() != dto.getSkillIds().size()) {
      throw new RuntimeException("Some skills not found");
    }

    job.setSkills(skills);

    JobPosting saved = jobPostingRepository.save(job);

    return toDTO(saved);
  }

  public JobPostingResponseDTO update(Integer id, JobPostingRequestDTO dto) {

    JobPosting job = jobPostingRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Job not found"));

    job.setTitle(dto.getTitle());
    job.setDescription(dto.getDescription());
    job.setRequirements(dto.getRequirements());

    job.setEmploymentType(dto.getEmploymentType());
    job.setWorkModel(dto.getWorkModel());

    job.setCity(dto.getCity());
    job.setState(dto.getState());
    job.setCountry(dto.getCountry());

    job.setSalaryMin(dto.getSalaryMin());
    job.setSalaryMax(dto.getSalaryMax());

    if (dto.getStatus() != null) {
      job.setStatus(dto.getStatus());
    }

    return toDTO(jobPostingRepository.save(job));
  }

  public void delete(Integer id) {
    jobPostingRepository.deleteById(id);
  }

  public Page<JobPostingResponseDTO> searchJobs(
    String query,
    WorkModel workModel,
    Double equityMin,
    Double equityMax,
    int page,
    int size
  ) {

    Pageable pageable = PageRequest.of(
      page,
      size,
      Sort.by("createdAt").descending()
    );

    return jobPostingRepository.searchJobs(
        query,
        workModel,
        equityMin,
        equityMax,
        pageable
      )
      .map(this::toDTO);
  }

  public List<JobSuggestionDTO> getSuggestions(
    String q,
    WorkModel workModel,
    Double equityMin,
    Double equityMax
  ) {
    Pageable limit = PageRequest.of(0, 5);

    return jobPostingRepository.findSuggestions(
        q,
        workModel,
        equityMin,
        equityMax,
        limit
      )
      .stream()
      .map(JobMapper::toSuggestionDTO)
      .toList();
  }

  public PaginatedResponseDTO<CandidateResponseDTO> getCandidatesForJob(
    UUID jobPublicId,
    User user,
    Pageable pageable
  ) {
    JobPosting job = jobPostingRepository.findByPublicId(jobPublicId)
      .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

    Startup startup = startupRepository.findByUser(user)
      .orElseThrow(() -> new ForbiddenAccessException("User does not have a startup"));

    if (!job.getStartup().getStartupId().equals(startup.getStartupId())) {
      throw new ForbiddenAccessException("You are not authorized to view candidates for this job");
    }

    Page<Application> applicationPage = applicationRepository.findApplicationsByJobPostingPublicId(jobPublicId, pageable);

    List<CandidateResponseDTO> candidateDTOs = applicationPage.getContent().stream()
      .map(Application::getCandidate)
      .map(CandidateMapper::toPartialCandidateDTO)
      .toList();

    return PaginatedResponseDTO.<CandidateResponseDTO>builder()
      .content(candidateDTOs)
      .page(applicationPage.getNumber())
      .pageSize(applicationPage.getSize())
      .totalElements(applicationPage.getTotalElements())
      .build();
  }

  private JobPostingResponseDTO toDTO(JobPosting job) {

    List<String> skills = job.getSkills() == null
      ? List.of()
      : job.getSkills().stream()
      .map(skill ->
        skill.getNomeExibicao() != null
          ? skill.getNomeExibicao()
          : skill.getNomeNormalizado()
      )
      .toList();

    return JobPostingResponseDTO.builder()
      .publicId(job.getPublicId())

      .startupId(job.getStartup().getPublicId())
      .startupName(job.getStartup().getCompanyName())

      .title(job.getTitle())
      .description(job.getDescription())
      .requirements(job.getRequirements())

      .skills(skills)

      .employmentType(job.getEmploymentType())
      .workModel(job.getWorkModel())

      .city(job.getCity())
      .state(job.getState())
      .country(job.getCountry())

      .salaryMin(job.getSalaryMin())
      .salaryMax(job.getSalaryMax())

      .equityMin(job.getEquityMin())
      .equityMax(job.getEquityMax())

      .jobStatus(job.getStatus())
      .createdAt(job.getCreatedAt())
      .candidateCount(applicationRepository.countByJobPosting_PublicId(job.getPublicId()))
      .build();
  }
}
