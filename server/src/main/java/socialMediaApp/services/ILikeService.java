package socialMediaApp.services;

import socialMediaApp.dto.requests.LikeRequest;
import socialMediaApp.dto.responses.like.LikeResponse;


import java.util.List;


public interface ILikeService {
    List<LikeResponse> getAllByPost(int postId);

    List<LikeResponse> getAllByUser(int userId);

    boolean isLiked(int userId,int postId);

    void add(LikeRequest likeRequest);

     void delete(LikeRequest likeRequest);

}
