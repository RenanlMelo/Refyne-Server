package com.renan.refyne.service;

import com.renan.refyne.dto.jobPosting.JobPostingRequestDTO;
import com.renan.refyne.dto.jobPosting.JobPostingResponseDTO;
import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import com.renan.refyne.enums.JobStatus;
import com.renan.refyne.repository.JobPostingRepository;
import com.renan.refyne.repository.StartupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostingService {

  private final JobPostingRepository jobPostingRepository;
  private final StartupRepository startupRepository;

  public JobPostingService(JobPostingRepository jobPostingRepository,
                           StartupRepository startupRepository) {
    this.jobPostingRepository = jobPostingRepository;
    this.startupRepository = startupRepository;
  }

  public JobPostingResponseDTO getById(Integer id) {
    JobPosting job = jobPostingRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Job not found"));

    return toDTO(job);
  }

  public List<JobPostingResponseDTO> getAll() {
    return jobPostingRepository.findAll()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  public List<JobPostingResponseDTO> getJobsByStartup(User user) {

    Startup startup = startupRepository.findByUser(user)
      .orElseThrow(() -> new RuntimeException("Startup not found"));

    List<JobPosting> jobs =
      jobPostingRepository.findByStartup_StartupId(startup.getStartupId());

    return jobs.stream()
      .map(this::toDTO)
      .toList();
  }

  public JobPostingResponseDTO create(JobPostingRequestDTO dto, User user) {

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
    job.setStartup(startup); // ✅ THIS IS THE KEY

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

    jobPostingRepository.save(job);

    return toDTO(job);
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

    JobPosting updated = jobPostingRepository.save(job);

    return toDTO(updated);
  }

  public void delete(Integer id) {
    jobPostingRepository.deleteById(id);
  }

  private JobPostingResponseDTO toDTO(JobPosting job) {
    return JobPostingResponseDTO.builder()
      .jobPostingId(job.getJobPostingId())
      .startupId(job.getStartup().getStartupId())
      .startupName(job.getStartup().getCompanyName())

      .title(job.getTitle())
      .description(job.getDescription())
      .requirements(job.getRequirements())

      .employmentType(job.getEmploymentType())
      .workModel(job.getWorkModel())

      .city(job.getCity())
      .state(job.getState())
      .country(job.getCountry())

      .salaryMin(job.getSalaryMin())
      .salaryMax(job.getSalaryMax())

      .jobStatus(job.getStatus())
      .createdAt(job.getCreatedAt())
      .build();
  }
}
