package mock.claimrequest.exception;

public class NoProjectForCurrentEmployeeException extends RuntimeException {

    public NoProjectForCurrentEmployeeException() {
        super("No project found for YOU. Please contact ADMIN to support it !");
    }

    public NoProjectForCurrentEmployeeException(String message) {
        super(message);
    }

    public NoProjectForCurrentEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProjectForCurrentEmployeeException(Throwable cause) {
        super(cause);
    }
}

