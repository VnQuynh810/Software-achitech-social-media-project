package socialMediaApp.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(int userId) {
        super("User with id " + userId + " not found");
    }
}