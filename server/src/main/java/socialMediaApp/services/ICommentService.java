package socialMediaApp.services;

import socialMediaApp.dto.requests.CommentAddRequest;
import socialMediaApp.dto.responses.comment.CommentGetResponse;

import java.util.List;

public interface ICommentService {

     void add(CommentAddRequest commentAddRequest) ;

     List<CommentGetResponse> getAll() ;

     List<CommentGetResponse> getAllByPost(int postId) ;

     List<CommentGetResponse> getAllByUser(int userId) ;

     void delete(int id);
}
