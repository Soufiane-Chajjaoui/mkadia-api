package fr.mkadia.mkadiaapi.exceptions;

public class EntityExistedException extends RuntimeException {
    public EntityExistedException() {
    }

    public EntityExistedException(String message) {
        super(message);
    }
}
