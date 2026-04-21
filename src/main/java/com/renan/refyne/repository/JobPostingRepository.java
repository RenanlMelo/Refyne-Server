package com.renan.refyne.repository;

import com.renan.refyne.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {
  List<JobPosting> findByStartup_StartupId(Long startupId);
}
