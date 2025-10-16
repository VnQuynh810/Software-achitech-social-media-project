package socialMediaApp.services.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import socialMediaApp.exception.InvalidDataException;
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
        User user = userRepository.findById(id).orElse(null);
        return userMapper.userToResponse(user);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserResponse getByEmail(String email){
        User user = userRepository.findByEmail(email);
        return userMapper.userToResponse(user);
    }

    @Override
    @Cacheable(value = "userFollowing", key = "#userId")
    public List<UserFollowingResponse> getUserFollowing(int userId){
        return userMapper.followsToFollowingResponses(followRepository.findAllByUser_Id(userId));
    }

    @Override
    @Cacheable(value = "isFollowing", key = "#userId + ':' + #followingId")
    public boolean isFollowing(int userId, int followingId){
        Optional<Follow> follow = followRepository.findByUser_IdAndFollowing_Id(userId, followingId);
        return follow.isPresent();
    }

    @Override
    @Cacheable(value = "usersEntity", key = "#id")
    public User getById(int id){
        return userRepository.findById(id).get();
    }

    @Override
    @CacheEvict(value = {"users", "usersEntity"}, allEntries = true)
    public void add(UserAddRequest userAddRequest){
        try {
            User user = userMapper.requestToUser(userAddRequest);
            userRepository.save(user);
        }
        catch (InvalidDataException e){
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = {"users", "usersEntity", "userFollowing", "isFollowing"}, allEntries = true)
    public void delete(int id){
        userRepository.deleteById(id);
    }
}