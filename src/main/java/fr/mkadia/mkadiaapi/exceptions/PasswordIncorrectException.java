package fr.mkadia.mkadiaapi.exceptions;

public class PasswordIncorrectException extends RuntimeException{
    public PasswordIncorrectException() {
    }
    public PasswordIncorrectException(String message){
        super(message);
    }
}
