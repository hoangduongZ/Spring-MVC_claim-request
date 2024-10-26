package mock.claimrequest.exception;

public class ProjectNotInProgressException extends RuntimeException{
    public ProjectNotInProgressException() {
        super("Project not in progress, please waiting start Project or you can contact ADMIN to know information detail !");
    }

    public ProjectNotInProgressException(String message) {
        super(message);
    }

    public ProjectNotInProgressException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectNotInProgressException(Throwable cause) {
        super(cause);
    }
}
