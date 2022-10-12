package exceptions;

public class GatewayNotFoundException extends Exception {
    public GatewayNotFoundException(String message) {
        super(message);
    }
}