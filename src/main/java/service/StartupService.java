package com.renan.refyne.service;

import com.renan.refyne.exception.auth.UserAlreadyInUseException;
import com.renan.refyne.repository.StartupRepository;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import org.springframework.stereotype.Service;
import dto.Startup.StartupRequestDTO;
import dto.Startup.StartupResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StartupService {

  private final StartupRepository startupRepository;

  public StartupService(StartupRepository startupRepository) {
    this.startupRepository = startupRepository;
  }

  public StartupResponseDTO getCandidateByCnpj(String cpf) {
    Startup startup = startupRepository.findByCnpj(cpf)
      .orElseThrow(() -> new RuntimeException("Candidate not found"));

    return toDTO(startup);
  }

  public List<StartupResponseDTO> getAllStartups() {
    return startupRepository.findAll()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  public StartupResponseDTO createStartupProfile(StartupRequestDTO dto, User user) {
    if (startupRepository.existsByCnpj(dto.getCnpj())) {
      throw new UserAlreadyInUseException("CNPJ");
    }

    Startup startup = new Startup();
    startup.setUser(user);
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

    return toDTO(startup);
  }

  private StartupResponseDTO toDTO(Startup startup) {
    return StartupResponseDTO.builder()
      .companyName(startup.getCompanyName())
      .description(startup.getDescription())
      .industry(startup.getIndustry())
      .stage(startup.getCity())
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
