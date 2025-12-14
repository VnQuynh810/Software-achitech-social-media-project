package com.example.likeservice.services;

import com.example.likeservice.dto.requests.LikeRequest;
import com.example.likeservice.dto.responses.LikeResponse;

import java.util.List;


public interface ILikeService {
    List<LikeResponse> getAllByPost(int postId);

    List<LikeResponse> getAllByUser(int userId);

    boolean isLiked(int userId,int postId);

    void add(LikeRequest likeRequest);

     void delete(LikeRequest likeRequest);

}
