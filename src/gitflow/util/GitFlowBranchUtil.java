package gitflow.util;

import com.intellij.openapi.project.Project;
import git4idea.GitUtil;
import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRepository;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public class GitFlowBranchUtil {

    public static boolean isCurrentBranchMaster(@NotNull Project project, @NotNull GitRepository repository) {
        GitFlowConfiguration gitFlowConfiguration = GitFlowConfiguration.getInstance(project);
        String currentBranchName = GitBranchUtil.getBranchNameOrRev(repository);
        return gitFlowConfiguration.getMasterBranch().equals(currentBranchName);
    }

    public static boolean isCurrentBranchDevelop(@NotNull Project project, @NotNull GitRepository repository) {
        GitFlowConfiguration gitFlowConfiguration = GitFlowConfiguration.getInstance(project);
        String currentBranchName = GitBranchUtil.getBranchNameOrRev(repository);
        return gitFlowConfiguration.getDevelopBranch().equals(currentBranchName);
    }

    public static boolean isCurrentBranchFeature(@NotNull Project project, @NotNull GitRepository repository) {
        GitFlowConfiguration gitFlowConfiguration = GitFlowConfiguration.getInstance(project);
        String currentBranchName = GitBranchUtil.getBranchNameOrRev(repository);
        return StringUtils.startsWith(currentBranchName, gitFlowConfiguration.getFeaturePrefix());
    }

    public static boolean isCurrentBranchHotfix(@NotNull Project project, @NotNull GitRepository repository) {
        GitFlowConfiguration gitFlowConfiguration = GitFlowConfiguration.getInstance(project);
        String currentBranchName = GitBranchUtil.getBranchNameOrRev(repository);
        return StringUtils.startsWith(currentBranchName, gitFlowConfiguration.getHotfixPrefix());
    }

    public static String getCurrentFeatureName(@NotNull Project project, @NotNull GitRepository repository) {
        GitFlowConfiguration gitFlowConfiguration = GitFlowConfiguration.getInstance(project);
        String currentBranchName = GitBranchUtil.getBranchNameOrRev(repository);
        String featurePrefix = gitFlowConfiguration.getFeaturePrefix();
        return StringUtils.removeStart(currentBranchName, featurePrefix);
    }

    public static String getCurrentHotfixName(@NotNull Project project, @NotNull GitRepository repository) {
        GitFlowConfiguration gitFlowConfiguration = GitFlowConfiguration.getInstance(project);
        String currentBranchName = GitBranchUtil.getBranchNameOrRev(repository);
        String hotfixPrefix = gitFlowConfiguration.getHotfixPrefix();
        return StringUtils.removeStart(currentBranchName, hotfixPrefix);
    }

    public static boolean isCurrentBranchNotPublished(@NotNull GitRepository repository) {
        String currentBranchName = GitBranchUtil.getBranchNameOrRev(repository);
        return GitUtil.findRemoteByName(repository, currentBranchName) == null;
    }
}
