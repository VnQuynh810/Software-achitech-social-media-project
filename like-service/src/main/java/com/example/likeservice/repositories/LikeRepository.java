package com.example.likeservice.repositories;


import com.example.likeservice.models.Like;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;
import java.util.Optional;

public interface LikeRepository extends MongoRepository<Like, Integer> {
    void deleteLikeById(int id);
    List<Like> findAllByPost_Id(int postId);
    List<Like> findAllByUser_Id(int userId);
    Optional<Like> findByUser_IdAndPost_Id(int userId,int postId);
}