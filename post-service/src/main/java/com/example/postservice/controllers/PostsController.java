package com.example.postservice.controllers;



import com.example.postservice.dto.requests.PostAddRequest;
import com.example.postservice.dto.responses.PostGetResponse;
import com.example.postservice.services.PostService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    private final PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<PostGetResponse>> getAll(){
        return new ResponseEntity<>(postService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<PostGetResponse> getById(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
        return new ResponseEntity<>(postService.getResponseById(id),HttpStatus.OK);
    }

    @GetMapping("/getallbyuser/{userId}")
    public ResponseEntity<List<PostGetResponse>> getAllByUser(@PathVariable int userId){
        return new ResponseEntity<>(postService.getAllByUser(userId),HttpStatus.OK);
    }

//    @GetMapping("/getbyuserfollowing/{userId}")
//    public ResponseEntity<List<PostGetResponse>> getAllByUserFollowing(@PathVariable int userId){
//        return new ResponseEntity<>(postService.getByUserFollowing(userId),HttpStatus.OK);
//    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody PostAddRequest postAddRequest){
        String postId = postService.add(postAddRequest);
        return new ResponseEntity<>(postId,HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
        postService.delete(id);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

}
