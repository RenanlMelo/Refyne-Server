package com.renan.refyne.service;

import com.renan.refyne.dto.jobPosting.JobPostingListDTO;
import com.renan.refyne.dto.jobPosting.JobPostingRequestDTO;
import com.renan.refyne.dto.jobPosting.JobPostingResponseDTO;
import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.entity.Skill;
import com.renan.refyne.entity.Startup;
import com.renan.refyne.entity.User;
import com.renan.refyne.repository.JobPostingRepository;
import com.renan.refyne.repository.SkillRepository;
import com.renan.refyne.repository.StartupRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final StartupRepository startupRepository;
    private final SkillRepository skillRepository;

    public JobPostingService(
            JobPostingRepository jobPostingRepository,
            StartupRepository startupRepository,
            SkillRepository skillRepository
    ) {
        this.jobPostingRepository = jobPostingRepository;
        this.startupRepository = startupRepository;
        this.skillRepository = skillRepository;
    }

    public List<JobPostingResponseDTO> getAll() {

        List<JobPostingListDTO> jobs = jobPostingRepository.findAllJobsDTO();

        if (jobs.isEmpty()) return List.of();

        List<Integer> ids = jobs.stream()
                .map(JobPostingListDTO::jobPostingId)
                .toList();

        List<Object[]> skillsRaw = jobPostingRepository.findSkillsByJobIds(ids);

        Map<Integer, List<String>> skillsMap = new HashMap<>();

        for (Object[] row : skillsRaw) {
            Integer jobId = (Integer) row[0];
            String skill = (String) row[1];

            skillsMap
                    .computeIfAbsent(jobId, k -> new ArrayList<>())
                    .add(skill);
        }

        return jobs.stream()
                .map(job -> JobPostingResponseDTO.builder()
                        .jobPostingId(job.jobPostingId())
                        .title(job.title())
                        .description(job.description())
                        .startupName(job.companyName())
                        .skills(skillsMap.getOrDefault(job.jobPostingId(), List.of()))
                        .build()
                )
                .toList();
    }

    public JobPostingResponseDTO createJob(JobPostingRequestDTO dto, User user) {

        Startup startup = startupRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User does not have a startup"));

        JobPosting job = new JobPosting();
        job.setStartup(startup);

        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setRequirements(dto.getRequirements());
        job.setEmploymentType(dto.getEmploymentType());
        job.setWorkModel(dto.getWorkModel());

        job.setCity(dto.getCity());
        job.setState(dto.getState());
        job.setCountry(dto.getCountry());

        job.setSalaryMin(dto.getSalaryMin());
        job.setSalaryMax(dto.getSalaryMax());
        job.setEquityMin(dto.getEquityMin());
        job.setEquityMax(dto.getEquityMax());

        List<Skill> skills = skillRepository.findAllById(dto.getSkillIds());
        job.setSkills(skills);

        JobPosting saved = jobPostingRepository.save(job);

        return JobPostingResponseDTO.builder()
                .jobPostingId(saved.getJobPostingId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .startupName(startup.getCompanyName())
                .skills(
                        skills.stream()
                                .map(Skill::getNomeExibicao)
                                .toList()
                )
                .build();
    }
}