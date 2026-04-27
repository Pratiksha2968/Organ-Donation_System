package com.organdonation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "donors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String bloodGroup;

    @ElementCollection
    @CollectionTable(name = "donor_organs", joinColumns = @JoinColumn(name = "donor_id"))
    @Column(name = "organ")
    private List<String> organsToDonate;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    private String country = "India";

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String currentMedications;

    private Boolean isLivingDonor = true;

    private Boolean isEmergencyAvailable = true;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus = KycStatus.PENDING;

    private LocalDate lastHealthCheckup;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
