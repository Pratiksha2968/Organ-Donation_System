package com.organdonation.repository;

import com.organdonation.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByUserId(Long userId);

    Optional<Hospital> findByRegistrationNumber(String registrationNumber);

    List<Hospital> findByCity(String city);

    List<Hospital> findByIsVerifiedTrue();

    @Query("SELECT COUNT(h) FROM Hospital h WHERE h.isVerified = true")
    Long countVerifiedHospitals();
}
