package com.example.profileservice.controllers;

import com.example.profileservice.models.UserEntitySql;
import com.example.profileservice.repositories.UserProfileJpaRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerSql {


    private final UserProfileJpaRepository mySqlRepo;

    public UserControllerSql(UserProfileJpaRepository mySqlRepo) {
        this.mySqlRepo = mySqlRepo;
    }

    @GetMapping("/mysql/users/{id}")
    public UserEntitySql getFromMySql(@PathVariable String id) {
        return mySqlRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found in MySQL"));
    }
}
