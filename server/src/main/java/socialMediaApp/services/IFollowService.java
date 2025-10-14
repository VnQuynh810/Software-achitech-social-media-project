package socialMediaApp.services;

import socialMediaApp.dto.requests.FollowRequest;


public interface IFollowService {
    void add(FollowRequest followAddRequest);

    void delete(FollowRequest followReques);
}
