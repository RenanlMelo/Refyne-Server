package com.renan.refyne.service;

import com.renan.refyne.dto.candidate.JobCandidateResponseDTO;
import com.renan.refyne.dto.candidate.PaginatedCandidateResponseDTO;
import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import com.renan.refyne.enums.AvailabilityStatus;
import com.renan.refyne.exception.global.JobNotFoundException;
import com.renan.refyne.exception.global.UnauthorizedAccessException;
import com.renan.refyne.repository.ApplicationRepository;
import com.renan.refyne.repository.CandidateRepository;
import com.renan.refyne.repository.JobPostingRepository;
import com.renan.refyne.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

  @Mock private ApplicationRepository applicationRepository;
  @Mock private JobPostingRepository jobPostingRepository;
  @Mock private StartupRepository startupRepository;
  @Mock private CandidateRepository candidateRepository;
  @Mock private FileUploadService fileUploadService;

  @InjectMocks private ApplicationService applicationService;

  private User user;
  private Startup startup;
  private JobPosting job;
  private UUID jobPublicId;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUserId(1L);

    startup = new Startup();
    startup.setStartupId(10L);

    job = new JobPosting();
    jobPublicId = UUID.randomUUID();
    job.setStartup(startup);
  }

  @Nested
  @DisplayName("getCandidatesByJob")
  class GetCandidatesByJob {

    private final Pageable defaultPageable = PageRequest.of(0, 10);

    @Test
    @DisplayName("returns paginated candidates for a valid request")
    void happyPath() {
      when(startupRepository.findByUser_UserId(1L)).thenReturn(Optional.of(startup));
      when(jobPostingRepository.findByPublicId(jobPublicId)).thenReturn(Optional.of(job));

      JobCandidateResponseDTO dto = JobCandidateResponseDTO.builder()
        .fullName("Alice Silva")
        .email("alice@example.com")
        .city("São Paulo")
        .state("SP")
        .country("Brazil")
        .resumeUrl("https://example.com/resume.pdf")
        .linkedinUrl("https://linkedin.com/in/alice")
        .portfolioUrl(null)
        .githubUrl("https://github.com/alice")
        .availabilityStatus(AvailabilityStatus.OPEN_TO_WORK)
        .build();

      Page<JobCandidateResponseDTO> page = new PageImpl<>(List.of(dto));
      when(applicationRepository.findCandidatesByJobPublicId(eq(jobPublicId), any(Pageable.class)))
        .thenReturn(page);

      PaginatedCandidateResponseDTO result =
        applicationService.getCandidatesByJob(jobPublicId, user, defaultPageable);

      assertThat(result.getCandidates()).hasSize(1);
      assertThat(result.getCandidates().get(0).getFullName()).isEqualTo("Alice Silva");
      assertThat(result.getCandidates().get(0).getEmail()).isEqualTo("alice@example.com");
      assertThat(result.getPage()).isEqualTo(0);
      assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("forwards the pageable directly to the repository")
    void pageableIsForwarded() {
      when(startupRepository.findByUser_UserId(1L)).thenReturn(Optional.of(startup));
      when(jobPostingRepository.findByPublicId(jobPublicId)).thenReturn(Optional.of(job));
      when(applicationRepository.findCandidatesByJobPublicId(eq(jobPublicId), eq(defaultPageable)))
        .thenReturn(new PageImpl<>(List.of()));

      applicationService.getCandidatesByJob(jobPublicId, user, defaultPageable);

      verify(applicationRepository).findCandidatesByJobPublicId(jobPublicId, defaultPageable);
    }

    @Test
    @DisplayName("throws JobNotFoundException when job does not exist")
    void jobNotFound() {
      when(startupRepository.findByUser_UserId(1L)).thenReturn(Optional.of(startup));
      when(jobPostingRepository.findByPublicId(jobPublicId)).thenReturn(Optional.empty());

      assertThatThrownBy(() ->
        applicationService.getCandidatesByJob(jobPublicId, user, defaultPageable)
      ).isInstanceOf(JobNotFoundException.class);
    }

    @Test
    @DisplayName("throws UnauthorizedAccessException when user has no startup")
    void userHasNoStartup() {
      when(startupRepository.findByUser_UserId(1L)).thenReturn(Optional.empty());

      assertThatThrownBy(() ->
        applicationService.getCandidatesByJob(jobPublicId, user, defaultPageable)
      ).isInstanceOf(UnauthorizedAccessException.class)
        .hasMessageContaining("startup profile");
    }

    @Test
    @DisplayName("throws UnauthorizedAccessException when user does not own the job")
    void notOwner() {
      Startup otherStartup = new Startup();
      otherStartup.setStartupId(99L);

      when(startupRepository.findByUser_UserId(1L)).thenReturn(Optional.of(otherStartup));
      when(jobPostingRepository.findByPublicId(jobPublicId)).thenReturn(Optional.of(job));

      assertThatThrownBy(() ->
        applicationService.getCandidatesByJob(jobPublicId, user, defaultPageable)
      ).isInstanceOf(UnauthorizedAccessException.class)
        .hasMessageContaining("do not own");
    }

    @Test
    @DisplayName("returns empty list when no candidates applied")
    void noCandidates() {
      when(startupRepository.findByUser_UserId(1L)).thenReturn(Optional.of(startup));
      when(jobPostingRepository.findByPublicId(jobPublicId)).thenReturn(Optional.of(job));
      when(applicationRepository.findCandidatesByJobPublicId(eq(jobPublicId), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of()));

      PaginatedCandidateResponseDTO result =
        applicationService.getCandidatesByJob(jobPublicId, user, defaultPageable);

      assertThat(result.getCandidates()).isEmpty();
      assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("returns empty list for page beyond results")
    void pageBeyondResults() {
      when(startupRepository.findByUser_UserId(1L)).thenReturn(Optional.of(startup));
      when(jobPostingRepository.findByPublicId(jobPublicId)).thenReturn(Optional.of(job));
      when(applicationRepository.findCandidatesByJobPublicId(eq(jobPublicId), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of()));

      PaginatedCandidateResponseDTO result =
        applicationService.getCandidatesByJob(jobPublicId, user, PageRequest.of(999, 10));

      assertThat(result.getCandidates()).isEmpty();
    }
  }
}
