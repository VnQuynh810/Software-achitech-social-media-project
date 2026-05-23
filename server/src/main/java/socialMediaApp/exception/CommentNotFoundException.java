package socialMediaApp.exception;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException(int commentId) {
        super("Comment with id " + commentId + " not found");
    }
}