package gitflow.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import git4idea.commands.GitCommandResult;
import git4idea.commands.GitImpl;
import git4idea.commands.GitLineHandler;
import git4idea.commands.GitLineHandlerListener;
import git4idea.commands.GitLineHandlerPasswordRequestAware;
import git4idea.repo.GitRepository;
import git4idea.util.GitUIUtil;
import gitflow.exception.GitFlowExecutionException;
import org.jetbrains.annotations.NotNull;

public class GitFlowImpl implements GitFlow {

    private static final Logger logger = Logger.getInstance(GitFlowImpl.class.getName());
    private static final String FEATURE = "feature";
    private static final String HOTFIX = "hotfix";

    private final Project myProject;

    public GitFlowImpl(@NotNull Project myProject) {
        this.myProject = myProject;
    }

    /**
     * Calls 'git flow init' on the specified directory.
     */
    @NotNull
    @Override
    public GitCommandResult initRepo(@NotNull GitRepository repository, boolean forceReinitialization) {
        // TODO: Add option to enter custom prefixes
        if (forceReinitialization) {
            return run(repository, "init", "-d", "-f");
        } else {
            return run(repository, "init", "-d");
        }
    }

    @NotNull
    @Override
    public GitCommandResult startFeature(@NotNull GitRepository repository, @NotNull String featureName) {
        return run(repository, FEATURE, "start", featureName);
    }

    @NotNull
    @Override
    public GitCommandResult finishFeature(@NotNull GitRepository repository, @NotNull String featureName) {
        return runPasswordRequestAware(repository, FEATURE, "finish", featureName);
    }

    @NotNull
    @Override
    public GitCommandResult publishFeature(@NotNull GitRepository repository, @NotNull String featureName) {
        return runPasswordRequestAware(repository, FEATURE, "publish", featureName);
    }

    @NotNull
    @Override
    public GitCommandResult pullFeature(@NotNull GitRepository repository, @NotNull String featureName) {
        return runPasswordRequestAware(repository, FEATURE, "pull", featureName);
    }

    @NotNull
    @Override
    public GitCommandResult trackFeature(@NotNull GitRepository repository, @NotNull String featureName) {
        return runPasswordRequestAware(repository, FEATURE, "track", featureName);
    }

    @NotNull
    @Override
    public GitCommandResult startHotfix(@NotNull GitRepository repository, @NotNull String hotfixName) {
        return run(repository, HOTFIX, "start", hotfixName);

    }

    @NotNull
    @Override
    public GitCommandResult finishHotfix(@NotNull GitRepository repository, @NotNull String hotfixName, @NotNull String tagMessage) {
        return runPasswordRequestAware(repository, HOTFIX, "finish", hotfixName);
    }

    @NotNull
    @Override
    public GitCommandResult publishHotfix(@NotNull GitRepository repository, @NotNull String hotfixName) {
        return runPasswordRequestAware(repository, HOTFIX, "publish", hotfixName);
    }

    @NotNull
    @Override
    public GitCommandResult trackHotfix(@NotNull GitRepository repository, @NotNull String hotfixName) {
        return runPasswordRequestAware(repository, HOTFIX, "track", hotfixName);
    }

    @NotNull
    private GitCommandResult runPasswordRequestAware(@NotNull GitRepository repository, @NotNull String... parameters) {
        final GitLineHandlerPasswordRequestAware handler = new GitLineHandlerPasswordRequestAware(repository.getProject(), repository.getRoot(), GitFlowCommand.FLOW);
        handler.setSilent(false);
        handler.addParameters(parameters);

        return run(handler);
    }

    @NotNull
    private GitCommandResult run(@NotNull GitRepository repository, @NotNull String... parameters) {
        final List<String> errorOutput = new ArrayList<String>();
        final GitLineHandler handler = new GitLineHandler(repository.getProject(), repository.getRoot(), GitFlowCommand.FLOW);
        handler.setSilent(false);
        handler.addParameters(parameters);

        handler.addLineListener(new GitLineHandlerListener() {
            @Override
            public void onLineAvailable(String line, Key outputType) {
                String errorMessage = getErrorMessage(line);
                if (errorMessage != null) {
                    errorOutput.add(errorMessage);
                }
            }

            @Override
            public void processTerminated(int code) {
                // nothing to do here
            }

            @Override
            public void startFailed(Throwable t) {
                // nothing to do here
            }
        });

        GitCommandResult result = run(handler);
        if (!errorOutput.isEmpty()) {
            GitUIUtil.notifyImportantError(myProject, "Error:", StringUtil.join(errorOutput, "<br/>"));
        }

        return result;
    }

    /**
     * {@link git4idea.commands.GitImpl#run} is private.
     * We have to use reflection to get access to it.
     */
    @NotNull
    private GitCommandResult run(@NotNull GitLineHandler handler) {
        GitCommandResult result;
        try {
            Method m = GitImpl.class.getDeclaredMethod("run", GitLineHandler.class);
            m.setAccessible(true);
            result = (GitCommandResult) m.invoke(null, handler);
        } catch (Exception e) {
            logger.error(e);
            throw new GitFlowExecutionException(e);
        }

        return result;
    }

    /**
     * Check if the line looks line an error message
     */
    private String getErrorMessage(String text) {
        for (GitFlowErrors error : GitFlowErrors.values()) {
            if (text.startsWith(error.getErrorCode())) {
                return error.getErrorMessage();
            }
        }
        return null;
    }

}
