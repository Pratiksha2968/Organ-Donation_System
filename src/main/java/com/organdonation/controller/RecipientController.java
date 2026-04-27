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
@RequestMapping("/recipient")
public class RecipientController {

    private final UserService userService;
    private final RecipientRepository recipientRepository;
    private final MatchingService matchingService;
    private final MatchRepository matchRepository;
    private final NotificationService notificationService;

    public RecipientController(UserService userService, RecipientRepository recipientRepository,
                               MatchingService matchingService, MatchRepository matchRepository,
                               NotificationService notificationService) {
        this.userService = userService;
        this.recipientRepository = recipientRepository;
        this.matchingService = matchingService;
        this.matchRepository = matchRepository;
        this.notificationService = notificationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);

        Recipient recipient = recipientRepository.findByUserId(user.getId()).orElse(null);
        model.addAttribute("recipient", recipient);

        if (recipient != null) {
            List<OrganMatch> matches = matchRepository.findByRecipientId(recipient.getId());
            model.addAttribute("matches", matches);
        }

        model.addAttribute("unreadNotifications", notificationService.getUnreadCount(user.getId()));

        return "recipient/dashboard";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("recipient", new Recipient());
        model.addAttribute("bloodGroups", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        model.addAttribute("organs", Arrays.asList("Kidney", "Liver", "Heart", "Lung", "Pancreas"));
        model.addAttribute("urgencyLevels", UrgencyLevel.values());
        return "recipient/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Recipient recipient, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        if (user != null) {
            recipient.setUser(user);
            recipient.setIsActive(true);
            recipientRepository.save(recipient);
        }
        return "redirect:/recipient/dashboard";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);

        Recipient recipient = recipientRepository.findByUserId(user.getId()).orElse(null);
        model.addAttribute("recipient", recipient);
        model.addAttribute("bloodGroups", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        model.addAttribute("organs", Arrays.asList("Kidney", "Liver", "Heart", "Lung", "Pancreas"));
        model.addAttribute("urgencyLevels", UrgencyLevel.values());

        return "recipient/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Recipient recipient, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        Recipient existingRecipient = recipientRepository.findByUserId(user.getId()).orElse(null);

        if (existingRecipient != null) {
            recipient.setId(existingRecipient.getId());
            recipient.setUser(user);
            recipientRepository.save(recipient);
        }

        return "redirect:/recipient/dashboard";
    }

    @GetMapping("/find-match")
    public String findMatch(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        Recipient recipient = recipientRepository.findByUserId(user.getId()).orElse(null);

        if (recipient != null) {
            List<MatchingService.MatchResult> matches = matchingService.findMatchesForRecipient(recipient.getId());
            model.addAttribute("matches", matches);
            model.addAttribute("recipient", recipient);
        }

        return "recipient/find-match";
    }

    @PostMapping("/quick-match")
    public String quickMatch(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        Recipient recipient = recipientRepository.findByUserId(user.getId()).orElse(null);

        if (recipient != null) {
            List<MatchingService.MatchResult> matches = matchingService.quickMatch(recipient.getId());
            model.addAttribute("matches", matches);
            model.addAttribute("quickMatch", true);
        }

        return "recipient/find-match";
    }

    @PostMapping("/create-match/{donorId}")
    public String createMatch(@PathVariable Long donorId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        Recipient recipient = recipientRepository.findByUserId(user.getId()).orElse(null);

        if (recipient != null) {
            matchingService.createMatch(donorId, recipient.getId(), null);
        }

        return "redirect:/recipient/dashboard";
    }

    @GetMapping("/notifications")
    public String notifications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("notifications", notificationService.getUserNotifications(user.getId()));
        return "recipient/notifications";
    }
}
