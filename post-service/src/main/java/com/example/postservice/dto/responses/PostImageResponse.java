package com.example.postservice.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostImageResponse {
    private String id;
    private String name;
    private String type;
    private byte[] data;
    private int postId;
}
