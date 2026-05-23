package com.example.postservice.repositories;

import com.example.postservice.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findAllByUserIdOrderByIdDesc(int userId);

}