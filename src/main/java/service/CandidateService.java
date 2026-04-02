package com.renan.refyne.service;

import com.renan.refyne.CandidateRepository;
import com.renan.refyne.entity.Candidate;
import com.renan.refyne.entity.User;
import com.renan.refyne.enums.UserType;
import dto.Candidate.CandidateRequestDTO;
import dto.Candidate.CandidateResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {

  private final CandidateRepository candidateRepository;

  public CandidateService(CandidateRepository candidateRepository) {
    this.candidateRepository = candidateRepository;
  }

  public CandidateResponseDTO getCandidateByCpf(String cpf) {
    Candidate candidate = candidateRepository.findByCpf(cpf)
      .orElseThrow(() -> new RuntimeException("Candidate not found"));

    return toDTO(candidate);
  }

  public List<CandidateResponseDTO> getAllCandidates() {
    return candidateRepository.findAll()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  public CandidateResponseDTO createCandidateProfile(CandidateRequestDTO dto, User user) {
    if (candidateRepository.existsByCpf(dto.getCpf())) {
      throw new IllegalArgumentException("CPF already registered");
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

    candidateRepository.save(candidate);

    return toDTO(candidate);
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
