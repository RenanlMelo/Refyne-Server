package com.renan.refyne.dto.skill;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SkillResponseDTO {

  String id;
  String nomeExibicao;
  String categoria;
  List<String> synonyms;

}
