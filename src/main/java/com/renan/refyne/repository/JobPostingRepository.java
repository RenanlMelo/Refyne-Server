package com.renan.refyne.repository;

import com.renan.refyne.dto.jobPosting.JobPostingListDTO;
import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.enums.WorkModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {
  @Query("""
  SELECT j FROM JobPosting j
  JOIN FETCH j.startup
  LEFT JOIN FETCH j.skills
  WHERE j.startup.publicId = :publicId
""")
  List<JobPosting> findByStartupPublicId(UUID publicId);

  @Query("""
  SELECT new com.renan.refyne.dto.jobPosting.JobPostingListDTO(
    j.publicId,
    j.title,
    j.description,
    s.companyName
  )
  FROM JobPosting j
  JOIN j.startup s
""")
  List<JobPostingListDTO> findAllJobsDTO();

  @Query("""
  SELECT j FROM JobPosting j
  JOIN FETCH j.startup
  LEFT JOIN FETCH j.skills
  WHERE j.publicId = :publicId
""")
  Optional<JobPosting> findByPublicId(UUID publicId);

  @Query("""
        SELECT j.publicId, s.nomeExibicao
        FROM JobPosting j
        JOIN j.skills s
        WHERE j.jobPostingId IN :ids
    """)
  List<Object[]> findSkillsByJobIds(@Param("ids") List<UUID> ids);

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
    @Param("query") String query,
    @Param("workModel") WorkModel workModel,
    @Param("equityMin") Double equityMin,
    @Param("equityMax") Double equityMax,
    Pageable pageable
  );

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
}
