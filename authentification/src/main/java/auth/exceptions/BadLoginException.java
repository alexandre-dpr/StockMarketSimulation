package auth.exceptions;

public class BadLoginException extends Exception {
    public BadLoginException(String s) {
        super(s);
    }
}
