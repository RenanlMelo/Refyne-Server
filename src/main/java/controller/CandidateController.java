package com.renan.refyne.controller;

import com.renan.refyne.entity.User;
import com.renan.refyne.service.CandidateService;
import dto.Candidate.CandidateRequestDTO;
import dto.Candidate.CandidateResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

  private final CandidateService candidateService;

  public CandidateController(CandidateService candidateService) {
    this.candidateService = candidateService;
  }

  @PostMapping("/create")
  public ResponseEntity<CandidateResponseDTO> createCandidate(
    @Valid @RequestBody CandidateRequestDTO dto,
    Authentication authentication
  ) {
    User user = (User) authentication.getPrincipal();

    CandidateResponseDTO response = candidateService.createCandidateProfile(dto, user);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{cpf}")
  public ResponseEntity<CandidateResponseDTO> getCandidateByCpf(@PathVariable String cpf) {
    CandidateResponseDTO response = candidateService.getCandidateByCpf(cpf);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<CandidateResponseDTO>> getAllCandidates() {
    List<CandidateResponseDTO> candidates = candidateService.getAllCandidates();
    return ResponseEntity.ok(candidates);
  }
}
