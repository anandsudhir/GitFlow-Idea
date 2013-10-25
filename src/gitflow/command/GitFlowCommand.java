package gitflow.command;

import java.lang.reflect.Method;

import com.intellij.openapi.diagnostic.Logger;
import git4idea.commands.GitCommand;
import gitflow.exception.GitFlowExecutionException;
import org.jetbrains.annotations.NotNull;

class GitFlowCommand {

    private static final Logger logger = Logger.getInstance(GitFlowCommand.class.getName());

    public static final GitCommand FLOW = write("flow");

    /**
     * {@link git4idea.commands.GitCommand#write} is private.
     * We have to use reflection to get access to it.
     */
    private static GitCommand write(@NotNull String gitflowCommandName) {
        GitCommand command;
        try {
            Method m = GitCommand.class.getDeclaredMethod("write", String.class);
            m.setAccessible(true);
            command = (GitCommand) m.invoke(null, gitflowCommandName);
        } catch (Exception e) {
            logger.error(e);
            throw new GitFlowExecutionException(e);
        }

        return command;
    }


}
