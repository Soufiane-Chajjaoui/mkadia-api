package fr.mkadia.mkadiaapi.exceptions;

public class StockInsuffisantException extends RuntimeException{
    public StockInsuffisantException() {}
    public StockInsuffisantException(String message) {
        super(message);
    }
}
