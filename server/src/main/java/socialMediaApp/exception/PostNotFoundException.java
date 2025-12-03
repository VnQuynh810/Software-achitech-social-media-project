package socialMediaApp.exception;

public class PostNotFoundException extends ResourceNotFoundException {
    public PostNotFoundException(int postId) {
        super("Post with id " + postId + " not found");
    }
}