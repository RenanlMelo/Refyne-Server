package com.renan.refyne.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SkillSeed {

  private Long id;

  @JsonProperty("nome_exibicao")
  private String nomeExibicao;

  @JsonProperty("nome_normalizado")
  private String nomeNormalizado;

  private String categoria;

  @JsonProperty("sinonimos")
  private List<String> sinonimos;
}
