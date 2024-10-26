package mock.claimrequest.exception;

public class TimeActiveEmployeeProjectNotValidException extends RuntimeException {

    public TimeActiveEmployeeProjectNotValidException() {
        super("The active time of Employee for the project is not valid.");
    }

    public TimeActiveEmployeeProjectNotValidException(String message) {
        super(message);
    }

    public TimeActiveEmployeeProjectNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}

