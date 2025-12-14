package com.example.profileservice.models;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntitySql {
    @Id
    @Column(name = "id")
    private String id; // Lưu UUID giống hệt bên Neo4j

    private String userId;
    private String firstName;
    private String lastName;
    private String city;
    private LocalDate dob;
}
