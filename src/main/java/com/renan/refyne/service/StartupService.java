package com.renan.refyne.service;

import com.renan.refyne.dto.user.ProfileCompletionResponseDTO;
import com.renan.refyne.repository.CandidateRepository;
import com.renan.refyne.exception.auth.UserAlreadyInUseException;
import com.renan.refyne.repository.StartupRepository;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import com.renan.refyne.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.renan.refyne.dto.startup.StartupRequestDTO;
import com.renan.refyne.dto.startup.StartupResponseDTO;
import com.renan.refyne.enums.UserType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StartupService {

  private final StartupRepository startupRepository;
  private final CandidateRepository candidateRepository;
  private final UserRepository userRepository;

  public StartupService(
    UserRepository userRepository,
    StartupRepository startupRepository,
    CandidateRepository candidateRepository
  ) {
    this.userRepository = userRepository;
    this.startupRepository = startupRepository;
    this.candidateRepository = candidateRepository;
  }

  public StartupResponseDTO getStartupById(Long id) {
    Startup startup = startupRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Startup not found"));

    return toDTO(startup);
  }

  public List<StartupResponseDTO> getAllStartups() {
    return startupRepository.findAll()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  @Transactional
  public ProfileCompletionResponseDTO createStartupProfile(StartupRequestDTO dto, User user) {

    User managedUser = userRepository.findById(user.getUserId())
      .orElseThrow(() -> new RuntimeException("User not found"));

    if (startupRepository.existsByCnpj(dto.getCnpj())) {
      throw new UserAlreadyInUseException("CNPJ");
    }

    if (startupRepository.existsByUser(user)) {
      throw new UserAlreadyInUseException("User already has a startup profile");
    }

    if (candidateRepository.existsByUser(user)) {
      throw new UserAlreadyInUseException("User already has a candidate profile");
    }

    if (user.getUserType() != UserType.STARTUP) {
      throw new RuntimeException("User is not a startup");
    }

    Startup startup = new Startup();
    startup.setUser(managedUser);
    startup.setCompanyName(dto.getCompanyName());
    startup.setDescription(dto.getDescription());
    startup.setIndustry(dto.getIndustry());
    startup.setStage(dto.getStage());
    startup.setFoundedDate(dto.getFoundedDate());
    startup.setSize(dto.getSize());
    startup.setLogoUrl(dto.getLogoUrl());
    startup.setWebsiteUrl(dto.getWebsiteUrl());
    startup.setLinkedinUrl(dto.getLinkedinUrl());
    startup.setCity(dto.getCity());
    startup.setState(dto.getState());
    startup.setCountry(dto.getCountry());
    startup.setCnpj(dto.getCnpj());

    startupRepository.save(startup);

    managedUser.setProfileCompleted(true);

    return new ProfileCompletionResponseDTO(true);
  }

  private StartupResponseDTO toDTO(Startup startup) {
    return StartupResponseDTO.builder()
      .companyName(startup.getCompanyName())
      .description(startup.getDescription())
      .industry(startup.getIndustry())
      .stage(startup.getStage())
      .foundedDate(startup.getFoundedDate())
      .size(startup.getSize())
      .logoUrl(startup.getLogoUrl())
      .websiteUrl(startup.getWebsiteUrl())
      .linkedinUrl(startup.getLinkedinUrl())
      .city(startup.getCity())
      .state(startup.getState())
      .country(startup.getCountry())
      .cnpj(startup.getCnpj())
      .build();
  }
}
