package com.renan.refyne.controller;

import com.renan.refyne.dto.candidate.JobCandidateResponseDTO;
import com.renan.refyne.dto.candidate.PaginatedCandidateResponseDTO;
import com.renan.refyne.entity.User;
import com.renan.refyne.enums.AvailabilityStatus;
import com.renan.refyne.enums.UserType;
import com.renan.refyne.exception.global.JobNotFoundException;
import com.renan.refyne.exception.global.UnauthorizedAccessException;
import com.renan.refyne.security.JwtAuthenticationFilter;
import com.renan.refyne.service.ApplicationService;
import com.renan.refyne.service.JobPostingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobPostingController.class)
@AutoConfigureMockMvc(addFilters = false)
class JobPostingControllerCandidatesTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private JobPostingService jobPostingService;
  @MockitoBean private ApplicationService applicationService;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  private UUID jobPublicId;
  private User user;

  @BeforeEach
  void setUp() {
    jobPublicId = UUID.randomUUID();

    user = new User();
    user.setUserId(1L);
    user.setEmail("startup@example.com");
    user.setPasswordHash("hash");
    user.setUserType(UserType.STARTUP);

    UsernamePasswordAuthenticationToken auth =
      new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @Nested
  @DisplayName("GET /api/jobs/{publicId}/candidates")
  class GetCandidatesByJob {

    @Test
    @DisplayName("returns 200 with paginated candidates")
    void happyPath() throws Exception {
      JobCandidateResponseDTO candidate = JobCandidateResponseDTO.builder()
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

      PaginatedCandidateResponseDTO response = PaginatedCandidateResponseDTO.builder()
        .candidates(List.of(candidate))
        .page(0)
        .pageSize(10)
        .totalElements(1)
        .build();

      when(applicationService.getCandidatesByJob(
        eq(jobPublicId), any(User.class), any(Pageable.class)
      )).thenReturn(response);

      mockMvc.perform(get("/api/jobs/{publicId}/candidates", jobPublicId)
          .param("page", "0")
          .param("size", "10")
          .principal(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.candidates").isArray())
        .andExpect(jsonPath("$.candidates.length()").value(1))
        .andExpect(jsonPath("$.candidates[0].fullName").value("Alice Silva"))
        .andExpect(jsonPath("$.candidates[0].email").value("alice@example.com"))
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.pageSize").value(10))
        .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("returns 404 when job does not exist")
    void jobNotFound() throws Exception {
      when(applicationService.getCandidatesByJob(
        eq(jobPublicId), any(User.class), any(Pageable.class)
      )).thenThrow(new JobNotFoundException());

      mockMvc.perform(get("/api/jobs/{publicId}/candidates", jobPublicId)
          .param("page", "0")
          .param("size", "10")
          .principal(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("JOB_NOT_FOUND"));
    }

    @Test
    @DisplayName("returns 403 when user is not the job owner")
    void unauthorized() throws Exception {
      when(applicationService.getCandidatesByJob(
        eq(jobPublicId), any(User.class), any(Pageable.class)
      )).thenThrow(new UnauthorizedAccessException("You do not own this job posting"));

      mockMvc.perform(get("/api/jobs/{publicId}/candidates", jobPublicId)
          .param("page", "0")
          .param("size", "10")
          .principal(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }

    @Test
    @DisplayName("returns 200 with empty candidates list when none found")
    void noCandidates() throws Exception {
      PaginatedCandidateResponseDTO response = PaginatedCandidateResponseDTO.builder()
        .candidates(List.of())
        .page(0)
        .pageSize(10)
        .totalElements(0)
        .build();

      when(applicationService.getCandidatesByJob(
        eq(jobPublicId), any(User.class), any(Pageable.class)
      )).thenReturn(response);

      mockMvc.perform(get("/api/jobs/{publicId}/candidates", jobPublicId)
          .param("page", "0")
          .param("size", "10")
          .principal(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.candidates").isEmpty())
        .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("returns 200 with empty list for page beyond available results")
    void pageBeyondResults() throws Exception {
      PaginatedCandidateResponseDTO response = PaginatedCandidateResponseDTO.builder()
        .candidates(List.of())
        .page(999)
        .pageSize(10)
        .totalElements(1)
        .build();

      when(applicationService.getCandidatesByJob(
        eq(jobPublicId), any(User.class), any(Pageable.class)
      )).thenReturn(response);

      mockMvc.perform(get("/api/jobs/{publicId}/candidates", jobPublicId)
          .param("page", "999")
          .param("size", "10")
          .principal(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.candidates").isEmpty())
        .andExpect(jsonPath("$.page").value(999));
    }

    @Test
    @DisplayName("returns 400 for invalid UUID path variable")
    void invalidUuid() throws Exception {
      mockMvc.perform(get("/api/jobs/{publicId}/candidates", "not-a-uuid")
          .param("page", "0")
          .param("size", "10")
          .principal(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())))
        .andExpect(status().isBadRequest());
    }
  }
}
