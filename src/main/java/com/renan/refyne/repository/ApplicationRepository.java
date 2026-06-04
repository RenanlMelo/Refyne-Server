package com.renan.refyne.repository;

import com.renan.refyne.dto.application.CandidateApplicationDTO;
import com.renan.refyne.dto.application.JobApplicationDetailDTO;
import com.renan.refyne.entity.Application;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

  @Query("SELECT COUNT(a) FROM Application a WHERE a.jobPosting.publicId = :jobPublicId")
  Long countByJobPublicId(@Param("jobPublicId") UUID jobPublicId);

  @Query("""
  SELECT new com.renan.refyne.dto.application.JobApplicationDetailDTO(
      a.publicId,
      a.status,
      a.appliedAt,
      a.coverLetter,
  
      c.publicId,
      c.fullName,
      u.email,
  
      c.headline,
      c.bio,
      a.resumeUrl,
  
      c.city,
      c.state,
      c.country,

      c.linkedinUrl,
      c.portfolioUrl,
      c.githubUrl,
      c.profilePhoto,
      c.availabilityStatus
  )
  FROM Application a
  JOIN a.candidate c
  JOIN c.user u
  WHERE a.jobPosting.publicId = :jobPublicId
  ORDER BY a.appliedAt DESC
  """)
  List<JobApplicationDetailDTO> findApplicationsByJob(UUID jobPublicId);

  @Query("""
  SELECT new com.renan.refyne.dto.application.CandidateApplicationDTO(
      a.publicId,
      j.title,
      s.companyName,
      a.status,
      a.appliedAt,
      j.publicId
  )
  FROM Application a
  JOIN a.jobPosting j
  JOIN j.startup s
  WHERE a.candidate.publicId = :candidatePublicId
  ORDER BY a.appliedAt DESC
  """)
  List<CandidateApplicationDTO> findApplicationsByCandidate(UUID candidatePublicId);

  boolean existsByJobPosting_PublicIdAndCandidate_PublicId(UUID jobPublicId, UUID candidatePublicId);
}
