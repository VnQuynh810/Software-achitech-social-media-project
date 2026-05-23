package com.example.followservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.validation.constraints.NotNull;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "follows")
public class Follow {
    @Id
    private int id;
    @NotNull

    Set<String> user;
    @NotNull
    Set<String> following;
}
