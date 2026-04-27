package com.organdonation.security;

import com.organdonation.entity.User;
import com.organdonation.repository.UserRepository;
import com.organdonation.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);

        // Set JWT as a cookie
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(86400); // 1 day
        response.addCookie(jwtCookie);

        // Redirect based on role
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (user != null) {
            switch (user.getRole()) {
                case ADMIN:
                    response.sendRedirect("/admin/dashboard");
                    return;
                case DONOR:
                    response.sendRedirect("/donor/dashboard");
                    return;
                case RECIPIENT:
                    response.sendRedirect("/recipient/dashboard");
                    return;
                case HOSPITAL:
                    response.sendRedirect("/hospital/dashboard");
                    return;
            }
        }
        response.sendRedirect("/dashboard");
    }
}
