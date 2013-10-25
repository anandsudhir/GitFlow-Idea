package gitflow.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import git4idea.branch.GitBranchUtil;
import git4idea.config.GitConfigUtil;
import git4idea.repo.GitRepository;
import git4idea.util.GitUIUtil;
import gitflow.ui.GitFlowConstants;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public class GitFlowConfigUtil {

    public static boolean isGitflowInitialized(@NotNull Project project) {
        GitRepository currentRepository = GitBranchUtil.getCurrentRepository(project);
        return (hasMasterConfigured(project, currentRepository) &&
                hasDevelopConfigured(project, currentRepository) &&
                hasPrefixesConfigured(project));
    }

    public static boolean hasMasterConfigured(@NotNull Project project, @NotNull GitRepository repository) {
        return GitBranchUtil.findLocalBranchByName(repository, getMasterBranch(project)) != null;
    }

    public static boolean hasDevelopConfigured(@NotNull Project project, @NotNull GitRepository repository) {
        return GitBranchUtil.findLocalBranchByName(repository, getDevelopBranch(project)) != null;
    }

    public static String getMasterBranch(@NotNull Project project) {
        String masterBranchName = getConfigValue(project, GitFlowConstants.BRANCH_MASTER);
        return StringUtils.defaultString(masterBranchName, GitFlowConstants.MASTER);
    }

    public static String getDevelopBranch(@NotNull Project project) {
        String masterBranchName = getConfigValue(project, GitFlowConstants.BRANCH_DEVELOP);
        return StringUtils.defaultString(masterBranchName, GitFlowConstants.DEVELOP);
    }

    public static boolean hasPrefixesConfigured(@NotNull Project project) {
        for (GitFlowConstants.PREFIXES prefix : GitFlowConstants.PREFIXES.values()) {
            if (getConfigValue(project, prefix.configKey()) == null) {
                return false;
            }
        }
        return true;
    }

    public static String getFeaturePrefix(@NotNull Project project) {
        return getConfigValue(project, GitFlowConstants.PREFIX_FEATURE);
    }

    public static String getReleasePrefix(@NotNull Project project) {
        return getConfigValue(project, GitFlowConstants.PREFIX_RELEASE);
    }

    public static String getHotfixPrefix(@NotNull Project project) {
        return getConfigValue(project, GitFlowConstants.PREFIX_HOTFIX);
    }

    public static String getSupportPrefix(@NotNull Project project) {
        return getConfigValue(project, GitFlowConstants.PREFIX_SUPPORT);
    }

    public static String getVersionTagPrefix(@NotNull Project project) {
        return getConfigValue(project, GitFlowConstants.PREFIX_VERSIONTAG);
    }

    private static String getConfigValue(@NotNull Project project, String configKey) {
        GitRepository currentRepository = GitBranchUtil.getCurrentRepository(project);
        try {
            return GitConfigUtil.getValue(project, currentRepository.getRoot(), configKey);
        } catch (VcsException e) {
            GitUIUtil.notifyError(project, "Git config error", null, true, e);
        }
        return null;
    }
}
