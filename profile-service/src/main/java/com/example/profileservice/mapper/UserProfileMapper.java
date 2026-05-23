package com.example.profileservice.mapper;

import com.example.profileservice.dto.requests.ProfileCreationRequest;
import com.example.profileservice.dto.responses.UserProfileResponse;
import com.example.profileservice.models.UserProfile;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileReponse(UserProfile entity);
}
