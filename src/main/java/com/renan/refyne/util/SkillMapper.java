package com.renan.refyne.util;

import com.renan.refyne.entity.Skill;
import com.renan.refyne.entity.SkillSynonym;
import com.renan.refyne.dto.skill.SkillResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SkillMapper {

  public static SkillResponseDTO toDTO(Skill skill) {

    List<String> synonyms = skill.getSynonyms() != null
      ? skill.getSynonyms()
      .stream()
      .map(SkillSynonym::getSynonym)
      .collect(Collectors.toList())
      : List.of();

    return SkillResponseDTO.builder()
      .id(skill.getId())
      .nomeExibicao(skill.getNomeExibicao())
      .categoria(skill.getCategoria())
      .synonyms(synonyms)
      .build();
  }
}
