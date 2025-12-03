package com.example.postservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.validation.constraints.NotNull;
import java.util.Set;


@Setter
@Getter
@Document(collection = "posts")
public class Post {

    @Id

    private String id;
    @NotNull

    private String description;
    @NotNull

    @JsonIgnore
    private int  userId;


    Set<Integer> likeId;


    Set<Integer> postImageId;


    Set<Integer> commentId;

}