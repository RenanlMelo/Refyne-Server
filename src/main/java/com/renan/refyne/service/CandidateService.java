package com.renan.refyne.service;

import com.renan.refyne.repository.CandidateRepository;
import com.renan.refyne.entity.Candidate;
import com.renan.refyne.entity.User;
import com.renan.refyne.exception.auth.UserAlreadyInUseException;
import com.renan.refyne.dto.candidate.CandidateRequestDTO;
import com.renan.refyne.dto.candidate.CandidateResponseDTO;
import com.renan.refyne.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.renan.refyne.enums.UserType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {

  private final CandidateRepository candidateRepository;
  private final UserRepository userRepository;

  public CandidateService(CandidateRepository candidateRepository, UserRepository userRepository) {
    this.candidateRepository = candidateRepository;
    this.userRepository = userRepository;
  }

  public CandidateResponseDTO getCandidateById(Long id) {
    Candidate candidate = candidateRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Candidate not found"));

    return toDTO(candidate);
  }

  public List<CandidateResponseDTO> getAllCandidates() {
    return candidateRepository.findAll()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  @Transactional
  public CandidateResponseDTO createCandidateProfile(CandidateRequestDTO dto) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User authUser  = (User) authentication.getPrincipal();

    User user = userRepository.findByUserId(authUser.getUserId())
      .orElseThrow(() -> new RuntimeException("User not found"));

    if (candidateRepository.existsByCpf(dto.getCpf())) {
      throw new UserAlreadyInUseException("CPF");
    }

    if (candidateRepository.existsByUser(user)) {
      throw new UserAlreadyInUseException("User already has a candidate profile");
    }

    if (user.getUserType() != UserType.CANDIDATE) {
      throw new RuntimeException("User is not a candidate");
    }

    Candidate candidate = new Candidate();
    candidate.setUser(user);
    candidate.setFullName(dto.getFullName());
    candidate.setHeadline(dto.getHeadline());
    candidate.setBio(dto.getBio());
    candidate.setCity(dto.getCity());
    candidate.setState(dto.getState());
    candidate.setCountry(dto.getCountry());
    candidate.setProfilePhoto(dto.getProfilePhoto());
    candidate.setResumeUrl(dto.getResumeUrl());
    candidate.setLinkedinUrl(dto.getLinkedinUrl());
    candidate.setPortfolioUrl(dto.getPortfolioUrl());
    candidate.setGithubUrl(dto.getGithubUrl());
    candidate.setAvailabilityStatus(dto.getAvailabilityStatus());
    candidate.setCpf(dto.getCpf());

    Candidate saved = candidateRepository.save(candidate);

    user.setProfileCompleted(true);
    userRepository.save(user);

    return toDTO(saved);
  }

  private CandidateResponseDTO toDTO(Candidate candidate) {
    return CandidateResponseDTO.builder()
      .fullName(candidate.getFullName())
      .headline(candidate.getHeadline())
      .bio(candidate.getBio())
      .city(candidate.getCity())
      .state(candidate.getState())
      .country(candidate.getCountry())
      .profilePhoto(candidate.getProfilePhoto())
      .resumeUrl(candidate.getResumeUrl())
      .linkedinUrl(candidate.getLinkedinUrl())
      .portfolioUrl(candidate.getPortfolioUrl())
      .githubUrl(candidate.getGithubUrl())
      .availabilityStatus(candidate.getAvailabilityStatus())
      .cpf(candidate.getCpf())
      .build();
  }
}
