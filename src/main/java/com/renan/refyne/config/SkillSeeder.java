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

            try {
                ObjectMapper mapper = new ObjectMapper();

                InputStream input = getClass()
                        .getResourceAsStream("/data/hard_skills.json");

                if (input == null) {
                    return;
                }

                SkillSeedWrapper wrapper =
                        mapper.readValue(input, SkillSeedWrapper.class);

                if (wrapper.getSkills() == null || wrapper.getSkills().isEmpty()) {
                    return;
                }

                int inserted = 0;
                int skipped = 0;

                for (SkillSeed seed : wrapper.getSkills()) {

                    if (repository.existsByNomeNormalizado(seed.getNomeNormalizado())) {
                        skipped++;
                        continue;
                    }

                    Skill skill = new Skill();
                    skill.setNomeExibicao(seed.getNomeExibicao());
                    skill.setNomeNormalizado(seed.getNomeNormalizado());
                    skill.setCategoria(seed.getCategoria());

                    List<SkillSynonym> synonymsList = new ArrayList<>();

                    if (seed.getSinonimos() != null) {
                        for (String syn : seed.getSinonimos()) {
                            SkillSynonym synonym = new SkillSynonym();
                            synonym.setSynonym(syn);
                            synonym.setSkill(skill);
                            synonymsList.add(synonym);
                        }
                    }

                    skill.setSynonyms(synonymsList);

                    repository.save(skill);
                    inserted++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}