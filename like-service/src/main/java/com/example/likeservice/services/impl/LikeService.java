package com.example.likeservice.services.impl;

import com.example.likeservice.dto.requests.LikeRequest;
import com.example.likeservice.dto.responses.LikeResponse;
import com.example.likeservice.mappers.LikeMapper;
import com.example.likeservice.models.Like;
import com.example.likeservice.repositories.LikeRepository;
import com.example.likeservice.services.ILikeService;
import org.springframework.stereotype.Service;




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
