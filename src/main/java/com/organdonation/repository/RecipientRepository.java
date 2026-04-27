package com.organdonation.repository;

import com.organdonation.entity.Recipient;
import com.organdonation.entity.UrgencyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    Optional<Recipient> findByUserId(Long userId);

    List<Recipient> findByBloodGroup(String bloodGroup);

    List<Recipient> findByRequiredOrgan(String organ);

    List<Recipient> findByUrgencyLevel(UrgencyLevel urgencyLevel);

    List<Recipient> findByIsActiveTrue();

    @Query("SELECT r FROM Recipient r WHERE r.isActive = true ORDER BY r.priorityScore DESC")
    List<Recipient> findAllActiveOrderByPriority();

    @Query("SELECT r FROM Recipient r WHERE r.bloodGroup = :bloodGroup AND r.requiredOrgan = :organ AND r.isActive = true ORDER BY r.priorityScore DESC")
    List<Recipient> findMatchingRecipients(@Param("bloodGroup") String bloodGroup, @Param("organ") String organ);

    @Query("SELECT COUNT(r) FROM Recipient r WHERE r.urgencyLevel = :urgency AND r.isActive = true")
    Long countByUrgencyLevel(@Param("urgency") UrgencyLevel urgency);

    @Query("SELECT r FROM Recipient r WHERE r.urgencyLevel = 'CRITICAL' AND r.isActive = true")
    List<Recipient> findCriticalPatients();
}
