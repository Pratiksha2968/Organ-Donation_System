package com.organdonation.config;

import com.organdonation.entity.*;
import com.organdonation.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DonorRepository donorRepository;
    private final RecipientRepository recipientRepository;
    private final HospitalRepository hospitalRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, DonorRepository donorRepository,
                          RecipientRepository recipientRepository, HospitalRepository hospitalRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.donorRepository = donorRepository;
        this.recipientRepository = recipientRepository;
        this.hospitalRepository = hospitalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create Admin
        if (!userRepository.existsByEmail("admin@organdonor.com")) {
            User admin = new User();
            admin.setEmail("admin@organdonor.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setIsVerified(true);
            userRepository.save(admin);
            System.out.println("✅ Admin created: admin@organdonor.com / admin123");
        }

        // Create Sample Hospital
        if (!userRepository.existsByEmail("hospital@cityhospital.com")) {
            User hospitalUser = new User();
            hospitalUser.setEmail("hospital@cityhospital.com");
            hospitalUser.setPassword(passwordEncoder.encode("hospital123"));
            hospitalUser.setRole(UserRole.HOSPITAL);
            hospitalUser.setFirstName("City");
            hospitalUser.setLastName("Hospital");
            hospitalUser.setIsVerified(true);
            hospitalUser = userRepository.save(hospitalUser);

            Hospital hospital = new Hospital();
            hospital.setUser(hospitalUser);
            hospital.setHospitalName("City General Hospital");
            hospital.setRegistrationNumber("HOSP001");
            hospital.setAddress("123 Medical Street");
            hospital.setCity("Mumbai");
            hospital.setState("Maharashtra");
            hospital.setContactNumber("022-12345678");
            hospital.setIsVerified(true);
            hospitalRepository.save(hospital);
            System.out.println("✅ Hospital created: hospital@cityhospital.com / hospital123");
        }

        // Create Sample Donor
        if (!userRepository.existsByEmail("donor@example.com")) {
            User donorUser = new User();
            donorUser.setEmail("donor@example.com");
            donorUser.setPassword(passwordEncoder.encode("donor123"));
            donorUser.setRole(UserRole.DONOR);
            donorUser.setFirstName("Rahul");
            donorUser.setLastName("Sharma");
            donorUser.setPhone("9876543210");
            donorUser.setIsVerified(true);
            donorUser = userRepository.save(donorUser);

            Donor donor = new Donor();
            donor.setUser(donorUser);
            donor.setBloodGroup("O+");
            donor.setOrgansToDonate(Arrays.asList("Kidney", "Liver"));
            donor.setCity("Mumbai");
            donor.setState("Maharashtra");
            donor.setDateOfBirth(LocalDate.of(1990, 5, 15));
            donor.setGender(Gender.MALE);
            donor.setIsLivingDonor(true);
            donor.setIsEmergencyAvailable(true);
            donor.setKycStatus(KycStatus.VERIFIED);
            donorRepository.save(donor);
            System.out.println("✅ Sample Donor created: donor@example.com / donor123");
        }

        // Create Sample Recipient
        if (!userRepository.existsByEmail("recipient@example.com")) {
            User recipientUser = new User();
            recipientUser.setEmail("recipient@example.com");
            recipientUser.setPassword(passwordEncoder.encode("recipient123"));
            recipientUser.setRole(UserRole.RECIPIENT);
            recipientUser.setFirstName("Priya");
            recipientUser.setLastName("Patel");
            recipientUser.setPhone("9876543211");
            recipientUser.setIsVerified(true);
            recipientUser = userRepository.save(recipientUser);

            Recipient recipient = new Recipient();
            recipient.setUser(recipientUser);
            recipient.setBloodGroup("O+");
            recipient.setRequiredOrgan("Kidney");
            recipient.setUrgencyLevel(UrgencyLevel.HIGH);
            recipient.setCity("Pune");
            recipient.setState("Maharashtra");
            recipient.setDateOfBirth(LocalDate.of(1985, 8, 20));
            recipient.setGender(Gender.FEMALE);
            recipient.setIsActive(true);
            recipientRepository.save(recipient);
            System.out.println("✅ Sample Recipient created: recipient@example.com / recipient123");
        }

        System.out.println("\n🚀 Data initialization completed!");
        System.out.println("==========================================");
        System.out.println("Login Credentials:");
        System.out.println("Admin: admin@organdonor.com / admin123");
        System.out.println("Hospital: hospital@cityhospital.com / hospital123");
        System.out.println("Donor: donor@example.com / donor123");
        System.out.println("Recipient: recipient@example.com / recipient123");
        System.out.println("==========================================\n");
    }
}
