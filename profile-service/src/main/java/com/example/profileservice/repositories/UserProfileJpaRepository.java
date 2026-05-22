package com.example.profileservice.repositories;


import com.example.profileservice.models.UserEntitySql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserEntitySql, String> {
}
