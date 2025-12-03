package com.example.postservice.mappers;

import com.example.postservice.dto.responses.PostImageResponse;
import com.example.postservice.models.PostImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PostImageMapper {

    @Mapping(source = "id",target = "postId")
    PostImageResponse imageToResponse(PostImage postImage);

}
