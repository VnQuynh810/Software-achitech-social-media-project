package com.example.profileservice;

import com.example.profileservice.models.UserProfile;
import com.example.profileservice.repositories.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class ProfileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApplication.class, args);
    }

    // Seed dữ liệu mẫu vào DB khi app start
    @Bean
    CommandLineRunner initDatabase(UserProfileRepository userProfileRepository) {
        return args -> {
            if (userProfileRepository.count() == 0) {
                UserProfile sample = UserProfile.builder()
                        .id(String.valueOf(1L))                      // 👈 GÁN ID TAY Ở ĐÂY
                        .userId("sample-user-01")
                        .firstName("QuynhSeed")
                        .lastName("NguyenSeed")
                        .dob(LocalDate.of(2004, 1, 1))
                        .city("Hanoi")
                        .build();

                userProfileRepository.save(sample);
                System.out.println(">>> Seeded sample user_profile record");
            } else {
                System.out.println(">>> user_profile already has data, skip seeding");
            }
        };
    }
}
