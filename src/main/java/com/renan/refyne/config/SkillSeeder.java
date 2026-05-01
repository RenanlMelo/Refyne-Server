package com.renan.refyne.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.refyne.entity.Skill;
import com.renan.refyne.entity.SkillSynonym;
import com.renan.refyne.repository.SkillRepository;
import com.renan.refyne.util.SkillSeed;
import com.renan.refyne.util.SkillSeedWrapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SkillSeeder {

  @Bean
  CommandLineRunner loadSkills(SkillRepository repository) {
    return args -> {

      if (repository.count() > 0) return;

      ObjectMapper mapper = new ObjectMapper();

      InputStream input = getClass()
        .getResourceAsStream("/data/hard_skills.json");

      SkillSeedWrapper wrapper =
        mapper.readValue(input, SkillSeedWrapper.class);

      for (SkillSeed seed : wrapper.getSkills()) {

        Skill skill = new Skill();
        skill.setNomeExibicao(seed.getNomeExibicao());
        skill.setNomeNormalizado(seed.getNomeNormalizado());
        skill.setCategoria(seed.getCategoria());

        var synonymsList = new ArrayList<SkillSynonym>();

        if (seed.getSinonimos() != null) {
          for (String syn : seed.getSinonimos()) {
            SkillSynonym synonym = new SkillSynonym();
            synonym.setSynonym(syn);
            synonym.setSkill(skill);
            synonymsList.add(synonym);
          }
        }

        skill.setSynonyms(synonymsList);

        List<Skill> skills = new ArrayList<>();

        repository.saveAll(skills);
      }
    };
  }
}
