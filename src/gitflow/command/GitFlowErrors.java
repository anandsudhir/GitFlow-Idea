package gitflow.command;

public enum GitFlowErrors {

    GITFLOW_NOT_INSTALLED("git: 'flow' is not a git command", "GitFlow is not installed"),
    GITFLOW_NOT_ENABLED("fatal: Not a gitflow-enabled repo yet", "Not a gitflow-enabled repo yet. Please init gitflow first");/*,
    MULTIPLE_HOTFIX_NOT_ALLOWED("There is an existing hotfix branch", "There is an existing hotfix branch. Finish that one first.");*/

    private final String errorCode;
    private final String errorMessage;

    GitFlowErrors(String code, String errorMessage) {
        this.errorCode = code;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
