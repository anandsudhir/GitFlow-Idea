package gitflow.ui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;

class GitFlowPopup {

    private final Project myProject;
    private final GitRepository myRepository;
    private final ListPopup myListPopup;

    static GitFlowPopup getInstance(@NotNull Project project, @NotNull GitRepository currentRepository) {
        return new GitFlowPopup(project, currentRepository);
    }

    private GitFlowPopup(@NotNull Project project, @NotNull GitRepository repository) {
        myProject = project;
        myRepository = repository;
        myListPopup = JBPopupFactory.getInstance().createActionGroupPopup("GitFlow", createActions(),
                SimpleDataContext.getProjectContext(project), false, false, true, null, -1, null);
    }

    ListPopup asListPopup() {
        return myListPopup;
    }

    private ActionGroup createActions() {
        DefaultActionGroup popupGroup = new DefaultActionGroup(null, false);
        return popupGroup;
    }

}
