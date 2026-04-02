package com.renan.refyne.controller;

import com.renan.refyne.entity.User;
import com.renan.refyne.service.StartupService;
import dto.Startup.StartupRequestDTO;
import dto.Startup.StartupResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/startup")
public class StartupController {

  private final StartupService startupService;

  public StartupController(StartupService startupService) {
    this.startupService = startupService;
  }

  @PostMapping
  public ResponseEntity<StartupResponseDTO> createCandidate(
    @Valid @RequestBody StartupRequestDTO dto,
    @RequestParam Long userId
  ) {
    User user = new User();
    user.setUserId(userId);

    StartupResponseDTO response = startupService.createStartupProfile(dto, user);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{cnpj}")
  public ResponseEntity<StartupResponseDTO> getCandidateByCpf(@PathVariable String cnpj) {
    StartupResponseDTO response = startupService.getCandidateByCnpj(cnpj);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<StartupResponseDTO>> getAllStartups() {
    List<StartupResponseDTO> candidates = startupService.getAllStartups();
    return ResponseEntity.ok(candidates);
  }
}
