package com.example.postservice.services;

import com.example.postservice.dto.requests.PostAddRequest;
import com.example.postservice.dto.responses.PostGetResponse;
import com.example.postservice.mappers.PostMapper;
import com.example.postservice.models.Post;
import com.example.postservice.repositories.PostRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class PostService {


    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }


    public List<PostGetResponse> getAll() {
        List<Post> posts = postRepository.findAll();
        return postMapper.postsToGetResponses(posts);
    }

    /**
     * Lấy post response theo ID
     */

    public PostGetResponse getResponseById(String id) throws ChangeSetPersister.NotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        return postMapper.postToGetResponse(post);
    }

    /**
     * Lấy post entity theo ID
     */

    public @NotNull Post getById(String id) throws ChangeSetPersister.NotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        return post;
    }

    /**
     * Lấy tất cả posts của một user
     */

    public List<PostGetResponse> getAllByUser(int userId) {


        List<Post> userPosts = postRepository.findAllByUserIdOrderByIdDesc(userId);
        return postMapper.postsToGetResponses(userPosts);
    }

    /**
     * Lấy posts từ những người user đang follow
     */

//    public List<PostGetResponse> getByUserFollowing(int userId) {
//
//
//
//
//        List<Post> posts = new ArrayList<>();
//
//        for (UserFollowingResponse user : follows) {
//            List<Post> userPosts = postRepository.findAllByUser_IdOrderByIdDesc(user.getUserId());
//            posts.addAll(userPosts);
//        }
//
//        // Sắp xếp theo ID giảm dần
//        posts.sort(Comparator.comparing(Post::getId).reversed());
//
//        return postMapper.postsToGetResponses(posts);
//    }



    public String add(PostAddRequest postAddRequest) {


        Post post = postMapper.postAddRequestToPost(postAddRequest);
        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }


    public void delete(String id) throws ChangeSetPersister.NotFoundException {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

        postRepository.delete(post);
    }
}
