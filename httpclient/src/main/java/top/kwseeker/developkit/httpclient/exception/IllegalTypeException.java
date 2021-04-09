package top.kwseeker.developkit.httpclient.exception;

public class IllegalTypeException extends RuntimeException {

    public IllegalTypeException() {
        super();
    }

    public IllegalTypeException(String message) {
        super(message);
    }

    public IllegalTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
