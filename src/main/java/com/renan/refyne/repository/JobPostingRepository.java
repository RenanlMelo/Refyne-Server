package com.renan.refyne.repository;

import com.renan.refyne.dto.jobPosting.JobPostingListDTO;
import com.renan.refyne.entity.JobPosting;
import com.renan.refyne.enums.WorkModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {

    @Query("""
    SELECT new com.renan.refyne.dto.jobPosting.JobPostingListDTO(
      j.jobPostingId,
      j.title,
      j.description,
      s.companyName
    )
    FROM JobPosting j
    JOIN j.startup s
  """)
    List<JobPostingListDTO> findAllJobsDTO();


    @Query("""
    SELECT j.jobPostingId, s.nomeExibicao
    FROM JobPosting j
    JOIN j.skills s
    WHERE j.jobPostingId IN :ids
  """)
    List<Object[]> findSkillsByJobIds(@Param("ids") List<Long> ids);


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