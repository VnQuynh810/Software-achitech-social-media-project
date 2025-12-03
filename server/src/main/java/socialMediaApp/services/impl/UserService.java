package socialMediaApp.services.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import socialMediaApp.exception.InvalidDataException;
import socialMediaApp.exception.ResourceNotFoundException;
import socialMediaApp.exception.UserNotFoundException;
import socialMediaApp.mappers.UserMapper;
import socialMediaApp.models.Follow;
import socialMediaApp.models.User;
import socialMediaApp.repositories.FollowRepository;
import socialMediaApp.repositories.UserRepository;
import socialMediaApp.dto.requests.UserAddRequest;
import socialMediaApp.dto.responses.user.UserFollowingResponse;
import socialMediaApp.dto.responses.user.UserResponse;
import socialMediaApp.services.IUserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository, FollowRepository followRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Override
    public List<UserResponse> getAll(){
        return userMapper.usersToResponses(userRepository.findAll());
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponse getResponseById(int id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.userToResponse(user);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserResponse getByEmail(String email){
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
        return userMapper.userToResponse(user);
    }

    @Override
    @Cacheable(value = "userFollowing", key = "#userId")
    public List<UserFollowingResponse> getUserFollowing(int userId){
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Follow> follows = followRepository.findAllByUser_Id(userId);
        return userMapper.followsToFollowingResponses(follows);
    }

    @Override
    @Cacheable(value = "isFollowing", key = "#userId + ':' + #followingId")
    public boolean isFollowing(int userId, int followingId) {
        // SỬA: Kiểm tra cả 2 user có tồn tại
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException(followingId));

        Optional<Follow> follow = followRepository.findByUser_IdAndFollowing_Id(userId, followingId);
        return follow.isPresent();
    }

    @Override
    @Cacheable(value = "usersEntity", key = "#id")
    public User getById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)); // SỬA: Thay .get() bằng orElseThrow
    }

    @Override
    @CacheEvict(value = {"users", "usersEntity"}, allEntries = true)
    public void add(UserAddRequest userAddRequest) {
        if (userRepository.findByEmail(userAddRequest.getEmail()) != null) {
            throw new InvalidDataException("Email already exists, Please try again!");
        }
        User user = userMapper.requestToUser(userAddRequest);
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = {"users", "usersEntity", "userFollowing", "isFollowing"}, allEntries = true)
    public void delete(int id) {
        // SỬA: Kiểm tra user có tồn tại trước khi xóa
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException( id));

        userRepository.delete(user); // Hoặc deleteById(id)
    }

    // THÊM: Method update user

}
