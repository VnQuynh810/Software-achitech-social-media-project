package socialMediaApp.services.impl;

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
    public UserResponse getResponseById(int id){
        User user = userRepository.findById(id).orElse(null);
        return userMapper.userToResponse(user);
    }
    @Override
    public UserResponse getByEmail(String email){
        User user = userRepository.findByEmail(email);
        return userMapper.userToResponse(user);
    }
    @Override
    public List<UserFollowingResponse> getUserFollowing(int userId){
        return userMapper.followsToFollowingResponses(followRepository.findAllByUser_Id(userId));
    }
    @Override
    public boolean isFollowing(int userId,int followingId){
       Optional<Follow> follow = followRepository.findByUser_IdAndFollowing_Id(userId,followingId);
       return follow.isPresent();
    }
    @Override
    public User getById(int id){
        return userRepository.findById(id).get();
    }
    @Override
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
    public void delete(int id){
        userRepository.deleteById(id);
    }
}
