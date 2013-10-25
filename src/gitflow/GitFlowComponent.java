package gitflow;

import com.intellij.dvcs.DvcsUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import git4idea.GitVcs;
import gitflow.ui.GitFlowWidget;
import org.jetbrains.annotations.NotNull;

public class GitFlowComponent implements ProjectComponent {

    private final Project myProject;

    public GitFlowComponent(@NotNull Project project) {
        myProject = project;
    }

    @Override
    public void projectOpened() {
        if (!ApplicationManager.getApplication().isHeadlessEnvironment() &&
                (ProjectLevelVcsManager.getInstance(myProject).findVcsByName(GitVcs.NAME) != null)) {
            DvcsUtil.installStatusBarWidget(myProject, GitFlowWidget.getInstance(myProject));
        }
    }

    @Override
    public void projectClosed() {
        GitFlowWidget.getInstance(myProject).dispose();
//        DvcsUtil.removeStatusBarWidget(myProject, GitFlowWidget.getInstance(myProject));
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "GitFlowComponent";
    }
}