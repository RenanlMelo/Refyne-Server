package com.renan.refyne.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "skill")
public class Skill {

  @Id
  @Column(length = 10)
  private String id;

  @Column(name = "nome_exibicao", nullable = false)
  private String nomeExibicao;

  @Column(name = "nome_normalizado", nullable = false, unique = true)
  private String nomeNormalizado;

  @Column(nullable = false)
  private String categoria;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SkillSynonym> synonyms;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
