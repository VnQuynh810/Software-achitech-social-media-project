package socialMediaApp.services.impl;

import org.springframework.stereotype.Service;
import socialMediaApp.mappers.LikeMapper;
import socialMediaApp.models.Like;
import socialMediaApp.repositories.LikeRepository;
import socialMediaApp.dto.requests.LikeRequest;
import socialMediaApp.dto.responses.like.LikeResponse;
import socialMediaApp.services.ILikeService;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService implements ILikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    public LikeService(LikeRepository likeRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
    }

    @Override
    public List<LikeResponse> getAllByPost(int postId){
        List<Like> likes = likeRepository.findAllByPost_Id(postId);
        return likeMapper.likesToLikeResponses(likes);
    }
    @Override
    public List<LikeResponse> getAllByUser(int userId){
        List<Like> likes = likeRepository.findAllByUser_Id(userId);
        return likeMapper.likesToLikeResponses(likes);
    }
    @Override
    public boolean isLiked(int userId,int postId){
        Optional<Like> like = likeRepository.findByUser_IdAndPost_Id(userId,postId);
        return like.isPresent();
    }
    @Override
    public void add(LikeRequest likeRequest){
        if (isLiked(likeRequest.getUserId(), likeRequest.getPostId())){
            return;
        }
        Like like = likeMapper.requestToLike(likeRequest);
        likeRepository.save(like);
    }
    @Override
    public void delete(LikeRequest likeRequest){
        Optional<Like> like = likeRepository.findByUser_IdAndPost_Id(likeRequest.getUserId(),likeRequest.getPostId());
       likeRepository.delete(like.get());
    }

}
