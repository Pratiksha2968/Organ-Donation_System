package com.organdonation.controller;

import com.organdonation.entity.*;
import com.organdonation.repository.*;
import com.organdonation.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/donor")
public class DonorController {

    private final UserService userService;
    private final DonorRepository donorRepository;
    private final MatchRepository matchRepository;
    private final NotificationService notificationService;

    public DonorController(UserService userService, DonorRepository donorRepository,
                          MatchRepository matchRepository, NotificationService notificationService) {
        this.userService = userService;
        this.donorRepository = donorRepository;
        this.matchRepository = matchRepository;
        this.notificationService = notificationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);

        Donor donor = donorRepository.findByUserId(user.getId()).orElse(null);
        model.addAttribute("donor", donor);

        if (donor != null) {
            List<OrganMatch> matches = matchRepository.findByDonorId(donor.getId());
            model.addAttribute("matches", matches);
        }

        model.addAttribute("unreadNotifications", notificationService.getUnreadCount(user.getId()));

        return "donor/dashboard";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("donor", new Donor());
        model.addAttribute("bloodGroups", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        model.addAttribute("organs", Arrays.asList("Kidney", "Liver", "Heart", "Lung", "Pancreas", "Cornea", "Bone", "Skin"));
        return "donor/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Donor donor, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        if (user != null) {
            donor.setUser(user);
            donor.setKycStatus(KycStatus.PENDING);
            donorRepository.save(donor);
        }
        return "redirect:/donor/dashboard";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);

        Donor donor = donorRepository.findByUserId(user.getId()).orElse(null);
        model.addAttribute("donor", donor);
        model.addAttribute("bloodGroups", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        model.addAttribute("organs", Arrays.asList("Kidney", "Liver", "Heart", "Lung", "Pancreas", "Cornea", "Bone", "Skin"));

        return "donor/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Donor donor, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        Donor existingDonor = donorRepository.findByUserId(user.getId()).orElse(null);

        if (existingDonor != null) {
            donor.setId(existingDonor.getId());
            donor.setUser(user);
            donorRepository.save(donor);
        }

        return "redirect:/donor/dashboard";
    }

    @PostMapping("/toggle-emergency")
    public String toggleEmergency(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        Donor donor = donorRepository.findByUserId(user.getId()).orElse(null);

        if (donor != null) {
            donor.setIsEmergencyAvailable(!donor.getIsEmergencyAvailable());
            donorRepository.save(donor);
        }

        return "redirect:/donor/dashboard";
    }

    @GetMapping("/notifications")
    public String notifications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("notifications", notificationService.getUserNotifications(user.getId()));
        return "donor/notifications";
    }
}
