package com.renan.refyne.controller;

import com.renan.refyne.dto.skill.SkillResponseDTO;
import com.renan.refyne.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

  private final SkillService service;

  public SkillController(SkillService service) {
    this.service = service;
  }

  @GetMapping("/search")
  public ResponseEntity<List<SkillResponseDTO>> search(@RequestParam String q) {
    return ResponseEntity.ok(service.searchSkills(q));
  }

  @GetMapping
  public ResponseEntity<List<SkillResponseDTO>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }
}
