package socialMediaApp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialMediaApp.dto.requests.UserAddRequest;
import socialMediaApp.dto.responses.user.UserResponse;
import socialMediaApp.services.IUserService;
import socialMediaApp.services.impl.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final IUserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<UserResponse>> getAll(){
        return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable int id){
        return new ResponseEntity<>(userService.getResponseById(id),HttpStatus.OK);
    }

    @GetMapping("/isfollowing")
    public ResponseEntity<Boolean> isFollowing(@RequestParam int userId,@RequestParam int followingId){
        return new ResponseEntity<>(userService.isFollowing(userId,followingId),HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody UserAddRequest userAddRequest){
            userService.add(userAddRequest);
            return new ResponseEntity<>("User Added", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam int id){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
