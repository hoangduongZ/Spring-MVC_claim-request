package mock.claimrequest.exception;

public class NoProjectForCurrentEmployee extends RuntimeException {

    public NoProjectForCurrentEmployee() {
        super("No project found for YOU. Please contact ADMIN to support it !");
    }

    public NoProjectForCurrentEmployee(String message) {
        super(message);
    }

    public NoProjectForCurrentEmployee(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProjectForCurrentEmployee(Throwable cause) {
        super(cause);
    }
}

