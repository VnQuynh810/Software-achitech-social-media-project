package com.example.profileservice.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_profile") // tên bảng trong PostgreSQL
public class UserProfile {

    @Id
    @Column(length = 36)
    String id;      // UUID dạng String, service sẽ tự set

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "dob")
    LocalDate dob;

    @Column(name = "city")
    String city;
}
