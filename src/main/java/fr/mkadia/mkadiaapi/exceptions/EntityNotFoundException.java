package fr.mkadia.mkadiaapi.exceptions;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String msg){
        super(msg);
    }
    public EntityNotFoundException(String msg , Exception e){
        super(msg , e);
    }

    public EntityNotFoundException() {
    }
}
