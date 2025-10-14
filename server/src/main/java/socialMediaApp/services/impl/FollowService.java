package socialMediaApp.services.impl;

import org.springframework.stereotype.Service;
import socialMediaApp.mappers.FollowMapper;
import socialMediaApp.models.Follow;
import socialMediaApp.repositories.FollowRepository;
import socialMediaApp.dto.requests.FollowRequest;
import socialMediaApp.services.IFollowService;


@Service
public class FollowService implements IFollowService {
    private final FollowRepository followRepository;
    private final FollowMapper followMapper;
    private final UserService userService;

    public FollowService(FollowRepository followRepository, FollowMapper followMapper, UserService userService) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;
        this.userService = userService;
    }

    @Override
    public void add(FollowRequest followAddRequest){
        if (userService.isFollowing(followAddRequest.getUserId(), followAddRequest.getFollowingId())){
            return;
        }
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
