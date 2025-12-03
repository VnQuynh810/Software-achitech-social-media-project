package com.example.postservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.NotNull;
import java.util.Optional;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class  PostImage {
    @Id
    private String id;
    private String name;
    private String type;
    private byte[] data;
    @NotNull
    @DBRef
    Post post;


}
