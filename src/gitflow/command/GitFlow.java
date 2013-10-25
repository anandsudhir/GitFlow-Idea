package gitflow.command;

import git4idea.commands.Git;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;

public interface GitFlow {

    @NotNull
    GitCommandResult initRepo(@NotNull GitRepository repository, boolean forceReinitialization);

    @NotNull
    GitCommandResult startFeature(@NotNull GitRepository repository, @NotNull String featureName);

    @NotNull
    GitCommandResult finishFeature(@NotNull GitRepository repository, @NotNull String featureName);

    @NotNull
    GitCommandResult publishFeature(@NotNull GitRepository repository, @NotNull String featureName);

    @NotNull
    GitCommandResult pullFeature(@NotNull GitRepository repository, @NotNull String featureName);

    @NotNull
    GitCommandResult trackFeature(@NotNull GitRepository repository, @NotNull String featureName);

    @NotNull
    GitCommandResult startHotfix(@NotNull GitRepository repository, @NotNull String hotfixName);

    @NotNull
    GitCommandResult finishHotfix(@NotNull GitRepository repository, @NotNull String hotfixName, @NotNull String tagMessage);

    @NotNull
    GitCommandResult publishHotfix(@NotNull GitRepository repository, @NotNull String featureName);

    @NotNull
    GitCommandResult trackHotfix(@NotNull GitRepository repository, @NotNull String featureName);

}