package socialMediaApp.services;

import socialMediaApp.dto.requests.PostAddRequest;
import socialMediaApp.dto.responses.post.PostGetResponse;
import socialMediaApp.models.Post;
import java.util.List;

public interface IPostService {
     List<PostGetResponse> getAll();

     PostGetResponse getResponseById(int id);

     Post getById(int id);

     List<PostGetResponse> getAllByUser(int userId);

     List<PostGetResponse> getByUserFollowing(int userId);

     int add(PostAddRequest postAddRequest);

     void delete(int id);
}
