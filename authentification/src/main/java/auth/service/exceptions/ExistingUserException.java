package auth.service.exceptions;

public class ExistingUserException extends Exception {
    public ExistingUserException(String s) {
        super(s);
    }
}
