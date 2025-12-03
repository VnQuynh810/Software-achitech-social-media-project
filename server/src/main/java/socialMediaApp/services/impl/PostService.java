package socialMediaApp.services.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import socialMediaApp.exception.PostNotFoundException;
import socialMediaApp.mappers.PostMapper;
import socialMediaApp.models.Post;
import socialMediaApp.repositories.PostRepository;
import socialMediaApp.dto.requests.PostAddRequest;
import socialMediaApp.dto.responses.post.PostGetResponse;
import socialMediaApp.dto.responses.user.UserFollowingResponse;
import socialMediaApp.services.IPostService;


import java.util.*;

@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    public PostService(PostRepository postRepository, PostMapper postMapper, UserService userService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userService = userService;
    }

    @Cacheable(value = "posts", key = "'all'")
    public List<PostGetResponse> getAll() {
        List<Post> posts = postRepository.findAll();
        return postMapper.postsToGetResponses(posts);
    }

    /**
     * Lấy post response theo ID
     */
    @Cacheable(value = "posts", key = "#id")
    public PostGetResponse getResponseById(int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return postMapper.postToGetResponse(post);
    }

    /**
     * Lấy post entity theo ID
     */
    @Cacheable(value = "postsEntity", key = "#id")
    public Post getById(int id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id)); // SỬA: Thay .get()
    }

    /**
     * Lấy tất cả posts của một user
     */
    @Cacheable(value = "userPosts", key = "#userId")
    public List<PostGetResponse> getAllByUser(int userId) {
        // SỬA: Kiểm tra user có tồn tại không
        userService.getById(userId); // Sẽ tự động ném UserNotFoundException nếu không tìm thấy

        List<Post> userPosts = postRepository.findAllByUser_IdOrderByIdDesc(userId);
        return postMapper.postsToGetResponses(userPosts);
    }

    /**
     * Lấy posts từ những người user đang follow
     */
    @Cacheable(value = "followingPosts", key = "#userId")
    public List<PostGetResponse> getByUserFollowing(int userId) {
        // SỬA: Kiểm tra user có tồn tại không
        userService.getById(userId); // Sẽ tự động ném UserNotFoundException

        List<UserFollowingResponse> follows = userService.getUserFollowing(userId);

        // SỬA: Kiểm tra danh sách follow rỗng
        if (follows.isEmpty()) {
            return new ArrayList<>(); // Trả về list rỗng thay vì null
        }

        List<Post> posts = new ArrayList<>();

        for (UserFollowingResponse user : follows) {
            List<Post> userPosts = postRepository.findAllByUser_IdOrderByIdDesc(user.getUserId());
            posts.addAll(userPosts);
        }

        // Sắp xếp theo ID giảm dần
        posts.sort(Comparator.comparing(Post::getId).reversed());

        return postMapper.postsToGetResponses(posts);
    }

    /**
     * Tạo post mới
     */
    @Caching(evict = {
            @CacheEvict(value = "posts", allEntries = true),
            @CacheEvict(value = "userPosts", key = "#postAddRequest.userId"),
            @CacheEvict(value = "followingPosts", allEntries = true)
    })
    public int add(PostAddRequest postAddRequest) {
        // SỬA: Validate user tồn tại
        userService.getById(postAddRequest.getUserId()); // Ném UserNotFoundException nếu không tìm thấy

        Post post = postMapper.postAddRequestToPost(postAddRequest);
        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    /**
     * Xóa post
     */
    @Caching(evict = {
            @CacheEvict(value = "posts", key = "#id"),
            @CacheEvict(value = "posts", key = "'all'"),
            @CacheEvict(value = "postsEntity", key = "#id"),
            @CacheEvict(value = "userPosts", allEntries = true),
            @CacheEvict(value = "followingPosts", allEntries = true)
    })
    public void delete(int id) {
        // SỬA: Kiểm tra post có tồn tại không
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postRepository.delete(post); // Hoặc deleteById(id)
    }
}
