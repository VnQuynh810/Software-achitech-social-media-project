package com.example.postservice.mappers;

import com.example.postservice.dto.requests.PostAddRequest;
import com.example.postservice.dto.responses.PostGetResponse;
import com.example.postservice.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "userId",target = "userId")
    PostGetResponse postToGetResponse(Post post);


    @Mapping(target = "id" , ignore = true)
    Post postAddRequestToPost(PostAddRequest postAddRequest);


    List<PostGetResponse> postsToGetResponses(List<Post> posts);
}
