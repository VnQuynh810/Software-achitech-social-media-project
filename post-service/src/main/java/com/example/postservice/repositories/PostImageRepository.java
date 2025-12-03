package com.example.postservice.repositories;

import com.example.postservice.models.PostImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostImageRepository extends MongoRepository<PostImage, String> {
    Optional<PostImage> findPostImageByPost_Id(String postId);
}