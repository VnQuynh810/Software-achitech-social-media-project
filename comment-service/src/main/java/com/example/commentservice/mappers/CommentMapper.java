package com.example.commentservice.mappers;

import com.example.commentservice.dto.requests.CommentAddRequest;
import com.example.commentservice.dto.responses.CommentGetResponse;
import com.example.commentservice.models.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.name",target = "userName")
    @Mapping(source = "user.lastName",target = "userLastName")
    CommentGetResponse commentToResponse(Comment comment);
    List<CommentGetResponse> commentsToResponses(List<Comment> comments);
    @Mapping(source = "userId",target = "user.id")
    @Mapping(source = "postId",target = "post.id")
    Comment addRequestToComment(CommentAddRequest commentAddRequest);
}
