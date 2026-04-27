package com.organdonation.controller;

import com.organdonation.entity.*;
import com.organdonation.repository.*;
import com.organdonation.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final DonorRepository donorRepository;
    private final RecipientRepository recipientRepository;
    private final HospitalRepository hospitalRepository;
    private final MatchRepository matchRepository;

    public AdminController(UserService userService, DonorRepository donorRepository,
                          RecipientRepository recipientRepository, HospitalRepository hospitalRepository,
                          MatchRepository matchRepository) {
        this.userService = userService;
        this.donorRepository = donorRepository;
        this.recipientRepository = recipientRepository;
        this.hospitalRepository = hospitalRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);

        // Statistics
        model.addAttribute("totalDonors", donorRepository.count());
        model.addAttribute("verifiedDonors", donorRepository.countVerifiedDonors());
        model.addAttribute("totalRecipients", recipientRepository.count());
        model.addAttribute("activeRecipients", recipientRepository.findByIsActiveTrue().size());
        model.addAttribute("criticalPatients", recipientRepository.findCriticalPatients().size());
        model.addAttribute("totalHospitals", hospitalRepository.count());
        model.addAttribute("verifiedHospitals", hospitalRepository.countVerifiedHospitals());
        model.addAttribute("pendingMatches", matchRepository.countByStatus(MatchStatus.PENDING));
        model.addAttribute("completedMatches", matchRepository.countSuccessfulTransplants());

        // Recent data
        model.addAttribute("recentDonors", donorRepository.findAll().stream().limit(5).toList());
        model.addAttribute("recentRecipients", recipientRepository.findAll().stream().limit(5).toList());
        model.addAttribute("recentMatches", matchRepository.findActiveMatches().stream().limit(5).toList());

        return "admin/dashboard";
    }

    @GetMapping("/donors")
    public String listDonors(Model model) {
        model.addAttribute("donors", donorRepository.findAll());
        return "admin/donors";
    }

    @GetMapping("/recipients")
    public String listRecipients(Model model) {
        model.addAttribute("recipients", recipientRepository.findAll());
        return "admin/recipients";
    }

    @GetMapping("/hospitals")
    public String listHospitals(Model model) {
        model.addAttribute("hospitals", hospitalRepository.findAll());
        return "admin/hospitals";
    }

    @GetMapping("/matches")
    public String listMatches(Model model) {
        model.addAttribute("matches", matchRepository.findAll());
        return "admin/matches";
    }

    @PostMapping("/verify-user/{userId}")
    public String verifyUser(@PathVariable Long userId) {
        userService.verifyUser(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/deactivate-user/{userId}")
    public String deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return "redirect:/admin/users";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        // Organ demand statistics
        Map<String, Long> organDemand = new HashMap<>();
        organDemand.put("Kidney", recipientRepository.findByRequiredOrgan("Kidney").stream().count());
        organDemand.put("Liver", recipientRepository.findByRequiredOrgan("Liver").stream().count());
        organDemand.put("Heart", recipientRepository.findByRequiredOrgan("Heart").stream().count());
        organDemand.put("Lung", recipientRepository.findByRequiredOrgan("Lung").stream().count());
        organDemand.put("Pancreas", recipientRepository.findByRequiredOrgan("Pancreas").stream().count());
        model.addAttribute("organDemand", organDemand);

        // Urgency distribution
        model.addAttribute("criticalCount", recipientRepository.countByUrgencyLevel(UrgencyLevel.CRITICAL));
        model.addAttribute("highCount", recipientRepository.countByUrgencyLevel(UrgencyLevel.HIGH));
        model.addAttribute("mediumCount", recipientRepository.countByUrgencyLevel(UrgencyLevel.MEDIUM));
        model.addAttribute("lowCount", recipientRepository.countByUrgencyLevel(UrgencyLevel.LOW));

        return "admin/analytics";
    }
}
