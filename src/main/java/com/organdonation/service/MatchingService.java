package com.organdonation.service;

import com.organdonation.entity.*;
import com.organdonation.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchingService {

    private final DonorRepository donorRepository;
    private final RecipientRepository recipientRepository;
    private final MatchRepository matchRepository;
    private final HospitalRepository hospitalRepository;
    private final NotificationService notificationService;

    // Blood group compatibility map
    private static final Map<String, List<String>> BLOOD_GROUP_COMPATIBILITY = new HashMap<>() {{
        put("O-", Arrays.asList("O-"));
        put("O+", Arrays.asList("O+", "O-"));
        put("A-", Arrays.asList("A-", "O-"));
        put("A+", Arrays.asList("A+", "A-", "O+", "O-"));
        put("B-", Arrays.asList("B-", "O-"));
        put("B+", Arrays.asList("B+", "B-", "O+", "O-"));
        put("AB-", Arrays.asList("AB-", "A-", "B-", "O-"));
        put("AB+", Arrays.asList("AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-"));
    }};

    public MatchingService(DonorRepository donorRepository, RecipientRepository recipientRepository,
                          MatchRepository matchRepository, HospitalRepository hospitalRepository,
                          NotificationService notificationService) {
        this.donorRepository = donorRepository;
        this.recipientRepository = recipientRepository;
        this.matchRepository = matchRepository;
        this.hospitalRepository = hospitalRepository;
        this.notificationService = notificationService;
    }

    /**
     * Smart Matching Algorithm - AI-based scoring system
     * Factors: Blood group compatibility, Organ match, Location proximity, Urgency priority
     */
    public List<MatchResult> findMatchesForRecipient(Long recipientId) {
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        List<Donor> potentialDonors = findCompatibleDonors(recipient);
        
        List<MatchResult> results = new ArrayList<>();
        
        for (Donor donor : potentialDonors) {
            BigDecimal score = calculateMatchScore(donor, recipient);
            
            MatchResult result = new MatchResult();
            result.setDonor(donor);
            result.setRecipient(recipient);
            result.setMatchScore(score);
            result.setBloodGroupMatch(isBloodGroupCompatible(donor.getBloodGroup(), recipient.getBloodGroup()));
            result.setOrganMatch(donor.getOrgansToDonate().contains(recipient.getRequiredOrgan()));
            result.setDistanceKm(calculateDistance(donor, recipient));
            
            results.add(result);
        }

        // Sort by match score descending
        results.sort((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()));
        
        return results;
    }

    /**
     * Quick Match for emergency cases
     */
    public List<MatchResult> quickMatch(Long recipientId) {
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Get emergency available donors
        List<Donor> emergencyDonors = donorRepository.findEmergencyAvailableDonors();
        
        List<MatchResult> results = new ArrayList<>();
        
        for (Donor donor : emergencyDonors) {
            if (isBloodGroupCompatible(donor.getBloodGroup(), recipient.getBloodGroup()) &&
                donor.getOrgansToDonate().contains(recipient.getRequiredOrgan())) {
                
                BigDecimal score = calculateMatchScore(donor, recipient);
                
                MatchResult result = new MatchResult();
                result.setDonor(donor);
                result.setRecipient(recipient);
                result.setMatchScore(score);
                result.setBloodGroupMatch(true);
                result.setOrganMatch(true);
                result.setDistanceKm(calculateDistance(donor, recipient));
                
                results.add(result);
            }
        }

        results.sort((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()));
        
        return results.stream().limit(5).collect(Collectors.toList());
    }

    /**
     * Create a match between donor and recipient
     */
    public OrganMatch createMatch(Long donorId, Long recipientId, Long hospitalId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        BigDecimal score = calculateMatchScore(donor, recipient);

        OrganMatch match = new OrganMatch();
        match.setDonor(donor);
        match.setRecipient(recipient);
        match.setMatchScore(score);
        match.setBloodGroupMatch(isBloodGroupCompatible(donor.getBloodGroup(), recipient.getBloodGroup()));
        match.setOrganMatch(donor.getOrgansToDonate().contains(recipient.getRequiredOrgan()));
        match.setDistanceKm(calculateDistance(donor, recipient));
        match.setStatus(MatchStatus.PENDING);
        match.setMatchedAt(LocalDateTime.now());

        if (hospitalId != null) {
            Hospital hospital = hospitalRepository.findById(hospitalId)
                    .orElseThrow(() -> new RuntimeException("Hospital not found"));
            match.setHospital(hospital);
        }

        match = matchRepository.save(match);

        // Send notifications
        notificationService.createNotification(
            donor.getUser().getId(),
            "Match Found!",
            "A potential recipient match has been found for your organ donation.",
            NotificationType.MATCH
        );
        
        notificationService.createNotification(
            recipient.getUser().getId(),
            "Match Found!",
            "A potential donor match has been found for your organ requirement.",
            NotificationType.MATCH
        );

        return match;
    }

    /**
     * Calculate AI-based match score (0-100)
     */
    private BigDecimal calculateMatchScore(Donor donor, Recipient recipient) {
        double score = 0.0;

        // Blood group compatibility (30 points)
        if (isBloodGroupCompatible(donor.getBloodGroup(), recipient.getBloodGroup())) {
            score += 30;
            // Bonus for exact match
            if (donor.getBloodGroup().equals(recipient.getBloodGroup())) {
                score += 5;
            }
        }

        // Organ match (25 points)
        if (donor.getOrgansToDonate().contains(recipient.getRequiredOrgan())) {
            score += 25;
        }

        // Urgency priority (20 points)
        switch (recipient.getUrgencyLevel()) {
            case CRITICAL:
                score += 20;
                break;
            case HIGH:
                score += 15;
                break;
            case MEDIUM:
                score += 10;
                break;
            case LOW:
                score += 5;
                break;
        }

        // Location proximity (15 points)
        Double distance = calculateDistance(donor, recipient);
        if (distance != null) {
            if (distance < 50) {
                score += 15;
            } else if (distance < 100) {
                score += 12;
            } else if (distance < 200) {
                score += 8;
            } else if (distance < 500) {
                score += 5;
            }
        }

        // Waiting time bonus (10 points)
        if (recipient.getWaitingSince() != null) {
            long waitingDays = ChronoUnit.DAYS.between(recipient.getWaitingSince(), LocalDateTime.now());
            if (waitingDays > 365) {
                score += 10;
            } else if (waitingDays > 180) {
                score += 7;
            } else if (waitingDays > 90) {
                score += 5;
            } else if (waitingDays > 30) {
                score += 3;
            }
        }

        return BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP);
    }

    private List<Donor> findCompatibleDonors(Recipient recipient) {
        List<Donor> allDonors = donorRepository.findVerifiedDonors();
        
        return allDonors.stream()
            .filter(d -> isBloodGroupCompatible(d.getBloodGroup(), recipient.getBloodGroup()))
            .filter(d -> d.getOrgansToDonate().contains(recipient.getRequiredOrgan()))
            .collect(Collectors.toList());
    }

    private boolean isBloodGroupCompatible(String donorBloodGroup, String recipientBloodGroup) {
        List<String> compatibleDonorGroups = BLOOD_GROUP_COMPATIBILITY.get(recipientBloodGroup);
        return compatibleDonorGroups != null && compatibleDonorGroups.contains(donorBloodGroup);
    }

    private Double calculateDistance(Donor donor, Recipient recipient) {
        if (donor.getLatitude() == null || donor.getLongitude() == null ||
            recipient.getLatitude() == null || recipient.getLongitude() == null) {
            return null;
        }

        // Haversine formula for distance calculation
        double lat1 = Math.toRadians(donor.getLatitude());
        double lat2 = Math.toRadians(recipient.getLatitude());
        double lon1 = Math.toRadians(donor.getLongitude());
        double lon2 = Math.toRadians(recipient.getLongitude());

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.pow(Math.sin(dlat / 2), 2) +
                   Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Earth's radius in kilometers
        return 6371 * c;
    }

    // Inner class for match result
    public static class MatchResult {
        private Donor donor;
        private Recipient recipient;
        private BigDecimal matchScore;
        private Boolean bloodGroupMatch;
        private Boolean organMatch;
        private Double distanceKm;

        // Getters and setters
        public Donor getDonor() { return donor; }
        public void setDonor(Donor donor) { this.donor = donor; }
        public Recipient getRecipient() { return recipient; }
        public void setRecipient(Recipient recipient) { this.recipient = recipient; }
        public BigDecimal getMatchScore() { return matchScore; }
        public void setMatchScore(BigDecimal matchScore) { this.matchScore = matchScore; }
        public Boolean getBloodGroupMatch() { return bloodGroupMatch; }
        public void setBloodGroupMatch(Boolean bloodGroupMatch) { this.bloodGroupMatch = bloodGroupMatch; }
        public Boolean getOrganMatch() { return organMatch; }
        public void setOrganMatch(Boolean organMatch) { this.organMatch = organMatch; }
        public Double getDistanceKm() { return distanceKm; }
        public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }
    }
}
