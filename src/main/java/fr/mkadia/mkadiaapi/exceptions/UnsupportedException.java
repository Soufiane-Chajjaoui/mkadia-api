package fr.mkadia.mkadiaapi.exceptions;

public class UnsupportedException extends RuntimeException{

    public UnsupportedException() {
        super();
    }

    public UnsupportedException(String message) {
        super(message);
    }

    public UnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
