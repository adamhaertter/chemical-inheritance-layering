package datasource.exceptions;

public class GatewayFailedToDelete extends Exception {
    public GatewayFailedToDelete(String message, Exception e) {
        super(message, e);
    }
}
