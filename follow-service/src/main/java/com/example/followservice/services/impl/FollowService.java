package com.example.followservice.services.impl;

import com.example.followservice.dto.requests.FollowRequest;
import com.example.followservice.mappers.FollowMapper;
import com.example.followservice.models.Follow;
import com.example.followservice.repositories.FollowRepository;
import com.example.followservice.services.IFollowService;
import org.springframework.stereotype.Service;




@Service
public class FollowService implements IFollowService {

    private final FollowRepository followRepository;
    private final FollowMapper followMapper;


    public FollowService(FollowRepository followRepository, FollowMapper followMapper) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;

    }

    @Override
    public void add(FollowRequest followAddRequest){

        followRepository.save(followMapper.addRequestToFollow(followAddRequest));
    }

    @Override
    public  void delete(FollowRequest followRequest){
      Follow follow
                = followRepository.findByUser_IdAndFollowing_Id(followRequest.getUserId(), followRequest.getFollowingId()).orElse(null);
        assert follow != null;
        followRepository.delete(follow);
    }


}
