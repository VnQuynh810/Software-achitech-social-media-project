package socialMediaApp.exception;

public class InvalidTokenException extends UnauthorizedException {
    public InvalidTokenException() {
        super("Invalid or expired token");
    }
}