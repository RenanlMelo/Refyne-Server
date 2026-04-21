package com.renan.refyne.controller;

import com.renan.refyne.entity.User;
import com.renan.refyne.service.StartupService;
import com.renan.refyne.dto.startup.StartupRequestDTO;
import com.renan.refyne.dto.startup.StartupResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/startups")
public class StartupController {

  private final StartupService startupService;

  public StartupController(StartupService startupService) {
    this.startupService = startupService;
  }

  @PostMapping("/create")
  public ResponseEntity<StartupResponseDTO> createCandidate(
    @Valid @RequestBody StartupRequestDTO dto,
    Authentication authentication
  ) {
    User user = (User) authentication.getPrincipal();

    StartupResponseDTO response = startupService.createStartupProfile(dto, user);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StartupResponseDTO> getCandidateByCpf(@PathVariable Long id) {
    StartupResponseDTO response = startupService.getStartupById(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<StartupResponseDTO>> getAllStartups() {
    List<StartupResponseDTO> candidates = startupService.getAllStartups();
    return ResponseEntity.ok(candidates);
  }
}
