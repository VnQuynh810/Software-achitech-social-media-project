package com.example.followservice.services;


import com.example.followservice.dto.requests.FollowRequest;

public interface IFollowService {
    void add(FollowRequest followAddRequest);

    void delete(FollowRequest followReques);
}
