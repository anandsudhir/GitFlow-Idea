package gitflow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import git4idea.commands.GitCommandResult;
import git4idea.repo.GitRepository;
import git4idea.util.GitUIUtil;
import gitflow.command.GitFlow;
import org.jetbrains.annotations.NotNull;

public class GitFlowService {

    private final Project myProject;
    private final GitFlow myGitFlow;

    private GitFlowService(@NotNull Project project, @NotNull GitFlow gitFlow) {
        this.myProject = project;
        this.myGitFlow = gitFlow;
    }

    public static GitFlowService getInstance(Project project) {
        return ServiceManager.getService(project, GitFlowService.class);
    }

    public void initRepo(@NotNull final GitRepository repository, final boolean forceReinitialization) {
        new Task.Backgroundable(myProject, "Initializing GitFlow", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                GitCommandResult result = myGitFlow.initRepo(repository, forceReinitialization);

                if (result.success()) {
                    GitUIUtil.notifySuccess(myProject, "", "Initialized GitFlow repo");
                } else {
                    notifyError(result);
                }

                repository.update();
            }

        }.queue();
    }

    public void startFeature(@NotNull final GitRepository repository, @NotNull final String newFeatureName) {
        new Task.Backgroundable(myProject, "Starting feature " + newFeatureName, false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                GitCommandResult result = myGitFlow.startFeature(repository, newFeatureName);

                if (result.success()) {
                    GitUIUtil.notifySuccess(myProject, "", "Created feature " + newFeatureName);
                } else {
                    notifyError(result);
                }

                repository.update();
            }
        }.queue();
    }

    public void finishFeature(@NotNull final GitRepository repository, @NotNull final String featureName) {
        new Task.Backgroundable(myProject, "Finishing feature " + featureName, true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                GitCommandResult result = myGitFlow.finishFeature(repository, featureName);

                if (result.success()) {
                    GitUIUtil.notifySuccess(myProject, "", "Finished feature " + featureName);
                } else {
                    notifyError(result);
                }

                repository.update();
            }
        }.queue();
    }

    public void publishFeature(@NotNull final GitRepository repository, @NotNull final String featureName) {
        new Task.Backgroundable(myProject, "Publishing feature " + featureName, true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                GitCommandResult result = myGitFlow.publishFeature(repository, featureName);

                if (result.success()) {
                    GitUIUtil.notifySuccess(myProject, "", "Published feature " + featureName);
                } else {
                    notifyError(result);
                }

                repository.update();
            }
        }.queue();
    }

    public void startHotfix(@NotNull final GitRepository repository, @NotNull final String newHotfixName) {
        new Task.Backgroundable(myProject, "Starting hotfix " + newHotfixName, false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                GitCommandResult result = myGitFlow.startHotfix(repository, newHotfixName);

                if (result.success()) {
                    GitUIUtil.notifySuccess(myProject, "", "Created hotfix " + newHotfixName);
                } else {
                    notifyError(result);
                }

                repository.update();
            }
        }.queue();
    }

    public void publishHotfix(@NotNull final GitRepository repository, @NotNull final String hotfixName) {
        new Task.Backgroundable(myProject, "Publishing hotfix " + hotfixName, true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                GitCommandResult result = myGitFlow.publishHotfix(repository, hotfixName);

                if (result.success()) {
                    GitUIUtil.notifySuccess(myProject, "", "Published hotfix " + hotfixName);
                } else {
                    notifyError(result);
                }

                repository.update();
            }
        }.queue();
    }

    public void finishHotfix(@NotNull final GitRepository repository, @NotNull final String hotfixName) {
        new Task.Backgroundable(myProject, "Finishing hotfix " + hotfixName, true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                // TODO: Add support for tagMessage
                GitCommandResult result = myGitFlow.finishHotfix(repository, hotfixName, "Finished feature" + hotfixName);

                if (result.success()) {
                    GitUIUtil.notifySuccess(myProject, "", "Finished hotfix " + hotfixName);
                } else {
                    notifyError(result);
                }

                repository.update();
            }
        }.queue();
    }

    // probably cant use something like this :(
    private class BackgroundableTask extends Task.Backgroundable {

        private final GitRepository myRepository;
        private final String successMessage;
        private final String[] parameters;

        public BackgroundableTask(@NotNull final Project project, @NotNull final GitRepository myRepository,
                                  @NotNull final String processTitle, @NotNull final String successMessage, @NotNull final String... parameters) {
            super(project, processTitle, true);
            this.myRepository = myRepository;
            this.successMessage = successMessage;
            this.parameters = parameters;
        }

        @Override
        public void run(@NotNull ProgressIndicator indicator) {
            GitCommandResult result = myGitFlow.startFeature(myRepository, parameters[0]);

            if (result.success()) {
                GitUIUtil.notifySuccess(myProject, "", successMessage);
            } else {
                notifyError(result);
            }

            myRepository.update();
        }
    }

    private void notifyError(GitCommandResult result) {
        GitUIUtil.notifyError(myProject, "Error", StringUtil.join(result.getOutputAsJoinedString(), "<br/>", result.getErrorOutputAsHtmlString()));
    }
}
