package gitflow.ui;

import javax.swing.Icon;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GitFlowAction extends DumbAwareAction {

    protected GitFlowAction() {
    }

    protected GitFlowAction(@Nullable String text) {
        super(text);
    }

    protected GitFlowAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (project == null || project.isDisposed()) {
            presentation.setEnabled(false);
            presentation.setVisible(false);
            return;
        }

        presentation.setEnabled(isEnabled(e));
    }

    /**
     * Checks if this action should be enabled.
     * Called in {@link #update(com.intellij.openapi.actionSystem.AnActionEvent)}, so don't execute long tasks here.
     *
     * @return true if the action is enabled.
     */
    protected boolean isEnabled(@NotNull AnActionEvent event) {
        return true;
    }

}