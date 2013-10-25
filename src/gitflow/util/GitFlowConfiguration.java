package gitflow.util;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;

public class GitFlowConfiguration {

    private final boolean gitflowInitialized;

    private final String masterBranch;
    private final String developBranch;
    private final String featurePrefix;
    private final String releasePrefix;
    private final String hotfixPrefix;
    private final String supportPrefix;
    private final String versionTagPrefix;

    private GitFlowConfiguration(@NotNull Project project) {
        gitflowInitialized = GitFlowConfigUtil.isGitflowInitialized(project);

        masterBranch = GitFlowConfigUtil.getMasterBranch(project);
        developBranch = GitFlowConfigUtil.getDevelopBranch(project);

        featurePrefix = GitFlowConfigUtil.getFeaturePrefix(project);
        releasePrefix = GitFlowConfigUtil.getReleasePrefix(project);
        hotfixPrefix = GitFlowConfigUtil.getHotfixPrefix(project);
        supportPrefix = GitFlowConfigUtil.getSupportPrefix(project);
        versionTagPrefix = GitFlowConfigUtil.getVersionTagPrefix(project);

    }

    public static GitFlowConfiguration getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, GitFlowConfiguration.class);
    }

    public String getVersionTagPrefix() {
        return versionTagPrefix;
    }

    public boolean isGitflowInitialized() {
        return gitflowInitialized;
    }

    public String getMasterBranch() {
        return masterBranch;
    }

    public String getDevelopBranch() {
        return developBranch;
    }

    public String getFeaturePrefix() {
        return featurePrefix;
    }

    public String getReleasePrefix() {
        return releasePrefix;
    }

    public String getHotfixPrefix() {
        return hotfixPrefix;
    }

    public String getSupportPrefix() {
        return supportPrefix;
    }
}
