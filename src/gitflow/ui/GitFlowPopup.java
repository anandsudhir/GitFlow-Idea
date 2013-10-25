package gitflow.ui;

import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;

class GitFlowPopup {

    private final ListPopup myListPopup;

    static GitFlowPopup getInstance(@NotNull Project project, @NotNull GitRepository repository) {
        return new GitFlowPopup(project, repository);
    }

    private GitFlowPopup(@NotNull Project project, @NotNull GitRepository repository) {
        myListPopup = JBPopupFactory.getInstance().createActionGroupPopup("GitFlow", new GitFlowPopupActions(project, repository).createActions(),
                SimpleDataContext.getProjectContext(project), false, false, true, null, -1, null);
    }

    ListPopup asListPopup() {
        return myListPopup;
    }

}
