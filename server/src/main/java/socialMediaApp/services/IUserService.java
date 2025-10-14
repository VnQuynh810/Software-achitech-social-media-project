package socialMediaApp.services;

import socialMediaApp.models.User;
import socialMediaApp.dto.requests.UserAddRequest;
import socialMediaApp.dto.responses.user.UserFollowingResponse;
import socialMediaApp.dto.responses.user.UserResponse;

import java.util.List;

public interface IUserService {


   List<UserResponse> getAll();

   UserResponse getResponseById(int id);

   UserResponse getByEmail(String email);

   List<UserFollowingResponse> getUserFollowing(int userId);

   boolean isFollowing(int userId,int followingId);

   User getById(int id);

   void add(UserAddRequest userAddRequest);

   void delete(int id);

}
