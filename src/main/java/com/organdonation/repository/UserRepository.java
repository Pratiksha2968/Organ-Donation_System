package com.organdonation.repository;

import com.organdonation.entity.User;
import com.organdonation.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByIsActiveTrue();

    List<User> findByIsVerifiedTrue();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(UserRole role);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
    List<User> findActiveUsersByRole(UserRole role);
}
