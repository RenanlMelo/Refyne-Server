package com.renan.refyne.dto.skill;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class SkillRequestDTO {

  @NotBlank
  private String id;

  @NotBlank
  private String nomeExibicao;

  @NotBlank
  private String nomeNormalizado;

  @NotBlank
  private String categoria;

  private List<String> synonyms;
}
