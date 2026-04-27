package com.organdonation.repository;

import com.organdonation.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

    Optional<Donor> findByUserId(Long userId);

    List<Donor> findByBloodGroup(String bloodGroup);

    List<Donor> findByCity(String city);

    List<Donor> findByIsEmergencyAvailableTrue();

    @Query("SELECT d FROM Donor d WHERE d.kycStatus = 'VERIFIED'")
    List<Donor> findVerifiedDonors();

    @Query("SELECT d FROM Donor d JOIN d.organsToDonate o WHERE o = :organ")
    List<Donor> findByOrganToDonate(@Param("organ") String organ);

    @Query("SELECT d FROM Donor d WHERE d.bloodGroup = :bloodGroup AND :organ MEMBER OF d.organsToDonate")
    List<Donor> findByBloodGroupAndOrgan(@Param("bloodGroup") String bloodGroup, @Param("organ") String organ);

    @Query("SELECT COUNT(d) FROM Donor d WHERE d.kycStatus = 'VERIFIED'")
    Long countVerifiedDonors();

    @Query("SELECT d FROM Donor d WHERE d.isEmergencyAvailable = true AND d.kycStatus = 'VERIFIED'")
    List<Donor> findEmergencyAvailableDonors();
}
