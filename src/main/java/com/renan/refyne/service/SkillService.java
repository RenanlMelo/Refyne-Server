package com.renan.refyne.service;

import com.renan.refyne.dto.skill.SkillResponseDTO;
import com.renan.refyne.entity.Skill;
import com.renan.refyne.repository.SkillRepository;
import com.renan.refyne.util.SkillMapper;
import com.renan.refyne.util.SkillNormalizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

  private final SkillRepository skillRepository;

  public SkillService(SkillRepository skillRepository) {
    this.skillRepository = skillRepository;
  }

  public List<SkillResponseDTO> getAll() {
    return skillRepository.findAll()
      .stream()
      .map(SkillMapper::toDTO)
      .collect(Collectors.toList());
  }

  public List<SkillResponseDTO> searchSkills(String input) {
    String normalized = SkillNormalizer.normalize(input);
    String term = "%" + normalized + "%";

    return skillRepository.searchSkills(term)
      .stream()
      .map(SkillMapper::toDTO)
      .toList();
  }
}
