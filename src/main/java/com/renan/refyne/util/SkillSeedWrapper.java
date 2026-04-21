package com.renan.refyne.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillSeedWrapper {

  private List<SkillSeed> skills;

  public List<SkillSeed> getSkills() {
    return skills;
  }

  public void setSkills(List<SkillSeed> skills) {
    this.skills = skills;
  }
}
