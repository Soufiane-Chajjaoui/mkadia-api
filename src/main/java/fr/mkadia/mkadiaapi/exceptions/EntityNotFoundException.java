package fr.mkadia.mkadiaapi.exceptions;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String msg){
        super(msg);
    }

    public EntityNotFoundException() {
    }
}
