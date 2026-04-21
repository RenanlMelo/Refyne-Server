package com.renan.refyne.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "skill_synonyms")
@Data
public class SkillSynonym {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String synonym;

  @ManyToOne
  @JoinColumn(name = "skill_id")
  private Skill skill;
}
