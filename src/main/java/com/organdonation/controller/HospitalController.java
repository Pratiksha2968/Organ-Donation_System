package com.organdonation.controller;

import com.organdonation.entity.*;
import com.organdonation.repository.*;
import com.organdonation.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/hospital")
public class HospitalController {

    private final UserService userService;
    private final HospitalRepository hospitalRepository;
    private final MatchRepository matchRepository;
    private final NotificationService notificationService;

    public HospitalController(UserService userService, HospitalRepository hospitalRepository,
                             MatchRepository matchRepository, NotificationService notificationService) {
        this.userService = userService;
        this.hospitalRepository = hospitalRepository;
        this.matchRepository = matchRepository;
        this.notificationService = notificationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);

        Hospital hospital = hospitalRepository.findByUserId(user.getId()).orElse(null);
        model.addAttribute("hospital", hospital);

        if (hospital != null) {
            List<OrganMatch> matches = matchRepository.findByHospitalId(hospital.getId());
            model.addAttribute("matches", matches);
        }

        model.addAttribute("unreadNotifications", notificationService.getUnreadCount(user.getId()));

        return "hospital/dashboard";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("hospital", new Hospital());
        return "hospital/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Hospital hospital, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        if (user != null) {
            hospital.setUser(user);
            hospital.setIsVerified(false);
            hospitalRepository.save(hospital);
        }
        return "redirect:/hospital/dashboard";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);

        Hospital hospital = hospitalRepository.findByUserId(user.getId()).orElse(null);
        model.addAttribute("hospital", hospital);

        return "hospital/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Hospital hospital, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        Hospital existingHospital = hospitalRepository.findByUserId(user.getId()).orElse(null);

        if (existingHospital != null) {
            hospital.setId(existingHospital.getId());
            hospital.setUser(user);
            hospitalRepository.save(hospital);
        }

        return "redirect:/hospital/dashboard";
    }

    @GetMapping("/matches")
    public String listMatches(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        Hospital hospital = hospitalRepository.findByUserId(user.getId()).orElse(null);

        if (hospital != null) {
            model.addAttribute("matches", matchRepository.findByHospitalId(hospital.getId()));
        }

        return "hospital/matches";
    }

    @PostMapping("/approve-match/{matchId}")
    public String approveMatch(@PathVariable Long matchId) {
        matchRepository.findById(matchId).ifPresent(match -> {
            match.setStatus(MatchStatus.APPROVED);
            match.setApprovedAt(LocalDateTime.now());
            matchRepository.save(match);

            // Notify donor and recipient
            notificationService.createNotification(
                match.getDonor().getUser().getId(),
                "Match Approved",
                "Your organ match has been approved by the hospital.",
                NotificationType.SUCCESS
            );
            notificationService.createNotification(
                match.getRecipient().getUser().getId(),
                "Match Approved",
                "Your organ match has been approved by the hospital.",
                NotificationType.SUCCESS
            );
        });

        return "redirect:/hospital/matches";
    }

    @PostMapping("/complete-match/{matchId}")
    public String completeMatch(@PathVariable Long matchId) {
        matchRepository.findById(matchId).ifPresent(match -> {
            match.setStatus(MatchStatus.COMPLETED);
            match.setCompletedAt(LocalDateTime.now());
            matchRepository.save(match);

            // Notify donor and recipient
            notificationService.createNotification(
                match.getDonor().getUser().getId(),
                "Transplant Completed",
                "The organ transplant has been completed successfully.",
                NotificationType.SUCCESS
            );
            notificationService.createNotification(
                match.getRecipient().getUser().getId(),
                "Transplant Completed",
                "Your organ transplant has been completed successfully.",
                NotificationType.SUCCESS
            );
        });

        return "redirect:/hospital/matches";
    }

    @GetMapping("/notifications")
    public String notifications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("notifications", notificationService.getUserNotifications(user.getId()));
        return "hospital/notifications";
    }
}
