package com.example.commentservice.services;



import com.example.commentservice.dto.responses.CommentGetResponse;
import com.example.commentservice.dto.requests.CommentAddRequest;
import java.util.List;

public interface ICommentService {

     void add(CommentAddRequest commentAddRequest) ;

     List<CommentGetResponse> getAll() ;

     List<CommentGetResponse> getAllByPost(int postId) ;

     List<CommentGetResponse> getAllByUser(int userId) ;

     void delete(int id);
}
