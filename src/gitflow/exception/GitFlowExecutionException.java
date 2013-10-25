package gitflow.exception;

/**
 * An error during executing a Git flow command. This is unchecked.
 */
public class GitFlowExecutionException extends RuntimeException {

    public GitFlowExecutionException(Throwable cause) {
        super(cause);
    }

    public GitFlowExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}