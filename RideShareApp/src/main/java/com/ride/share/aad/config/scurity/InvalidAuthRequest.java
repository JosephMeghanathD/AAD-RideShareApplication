public class InvalidAuthRequest extends Exception {
    private final String message;

    InvalidAuthRequest(String message) {
        this.message = message;
    }
}
