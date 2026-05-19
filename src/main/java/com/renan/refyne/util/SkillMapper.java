package com.renan.refyne.util;

import com.renan.refyne.entity.Skill;
import com.renan.refyne.entity.SkillSynonym;
import com.renan.refyne.dto.skill.SkillResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SkillMapper {

  public static SkillResponseDTO toDTO(Skill skill) {

    List<String> synonyms = List.of();

    if (skill.getSynonyms() != null) {
      synonyms = skill.getSynonyms()
        .stream()
        .map(SkillSynonym::getSynonym)
        .filter(s -> s != null && !s.isBlank())
        .map(String::trim)
        .distinct()
        .toList();
    }

    return SkillResponseDTO.builder()
      .id(skill.getId())
      .nomeExibicao(skill.getNomeExibicao())
      .categoria(skill.getCategoria())
      .synonyms(synonyms)
      .build();
  }
}
