package gitflow.ui;

import java.awt.event.MouseEvent;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryChangeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GitFlowWidget extends EditorBasedWidget implements StatusBarWidget.MultipleTextValuesPresentation,
        StatusBarWidget.Multiframe,
        GitRepositoryChangeListener {

    private static final String GIT_FLOW = "GitFlow";

    private GitFlowWidget(@NotNull Project project) {
        super(project);
    }

    public static GitFlowWidget getInstance(Project project) {
        return ServiceManager.getService(project, GitFlowWidget.class);
    }

    @Override
    public StatusBarWidget copy() {
        return new GitFlowWidget(getProject());
    }

    @Nullable
    @Override
    public ListPopup getPopupStep() {
        Project project = getProject();
        if (project == null) {
            return null;
        }
        GitRepository repo = GitBranchUtil.getCurrentRepository(project);
        if (repo == null) {
            return null;
        }

        return GitFlowPopup.getInstance(project, repo).asListPopup();

    }

    @Nullable
    @Override
    public String getSelectedValue() {
        return GIT_FLOW;
    }

    @NotNull
    @Override
    public String getMaxValue() {
        return GIT_FLOW;
    }

    @NotNull
    @Override
    public String ID() {
        return GitFlowWidget.class.getName();
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation(@NotNull PlatformType type) {
        return this;
    }

    @Nullable
    @Override
    public String getTooltipText() {
        return "GitFlow Integration";
    }

    @Nullable
    @Override
    public Consumer<MouseEvent> getClickConsumer() {
        return new Consumer<MouseEvent>() {
            public void consume(MouseEvent mouseEvent) {
                update();
            }
        };
    }

    @Override
    public void repositoryChanged(@NotNull GitRepository repository) {
        update();
    }

    private void update() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Project project = getProject();
                if (project == null) {
                    return;
                }

                GitRepository repo = GitBranchUtil.getCurrentRepository(project);
                if (repo == null) { // the file is not under version control => display nothing
                    return;
                }

                // TODO: Check if GitFlow is installed. Else, unregister this widget
                myStatusBar.updateWidget(ID());
            }
        });
    }
}
