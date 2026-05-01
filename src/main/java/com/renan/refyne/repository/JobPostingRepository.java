package com.renan.refyne.repository;

import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.entity.Skill;
import com.renan.refyne.enums.WorkModel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {

  Optional<JobPosting> findById (Integer id);

  @Query("""
  SELECT jp FROM JobPosting jp
  JOIN FETCH jp.startup
  LEFT JOIN FETCH jp.skills
  WHERE jp.startup.startupId = :id
""")
  List<JobPosting> findByStartupWithSkills(Long id);

  // Find Job Suggestions
  @Query("""
    SELECT j FROM JobPosting j
    JOIN j.startup s
    WHERE (
        LOWER(j.title) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(s.companyName) LIKE LOWER(CONCAT('%', :q, '%'))
    )
    AND (:workModel IS NULL OR j.workModel = :workModel)
    AND (:equityMin IS NULL OR j.equityMin >= :equityMin)
    AND (:equityMax IS NULL OR j.equityMax <= :equityMax)
    ORDER BY j.createdAt DESC
""")
  List<JobPosting> findSuggestions(
    @Param("q") String q,
    @Param("workModel") WorkModel workModel,
    @Param("equityMin") Double equityMin,
    @Param("equityMax") Double equityMax,
    Pageable pageable
  );

  @Query("""
    SELECT j FROM JobPosting j
    JOIN j.startup s
    WHERE (
        :query IS NULL OR
        LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(j.description) LIKE LOWER(CONCAT('%', :query, '%')) OR
        LOWER(s.companyName) LIKE LOWER(CONCAT('%', :query, '%'))
    )
    AND (:workModel IS NULL OR j.workModel = :workModel)
    AND (:equityMin IS NULL OR j.equityMin >= :equityMin)
    AND (:equityMax IS NULL OR j.equityMax <= :equityMax)
""")
  Page<JobPosting> searchJobs(
    String query,
    WorkModel workModel,
    Double equityMin,
    Double equityMax,
    Pageable pageable
  );
}
