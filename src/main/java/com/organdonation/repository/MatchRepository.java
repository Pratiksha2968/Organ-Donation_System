package com.organdonation.repository;

import com.organdonation.entity.MatchStatus;
import com.organdonation.entity.OrganMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<OrganMatch, Long> {

    List<OrganMatch> findByStatus(MatchStatus status);

    List<OrganMatch> findByDonorId(Long donorId);

    List<OrganMatch> findByRecipientId(Long recipientId);

    List<OrganMatch> findByHospitalId(Long hospitalId);

    @Query("SELECT m FROM OrganMatch m WHERE m.status IN ('PENDING', 'APPROVED', 'IN_PROGRESS') ORDER BY m.matchScore DESC")
    List<OrganMatch> findActiveMatches();

    @Query("SELECT COUNT(m) FROM OrganMatch m WHERE m.status = :status")
    Long countByStatus(@Param("status") MatchStatus status);

    @Query("SELECT COUNT(m) FROM OrganMatch m WHERE m.status = 'COMPLETED'")
    Long countSuccessfulTransplants();
}
