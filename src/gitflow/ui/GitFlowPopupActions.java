package gitflow.ui;

import java.util.Collections;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRepository;
import git4idea.ui.branch.GitMultiRootBranchConfig;
import git4idea.validators.GitNewBranchNameValidator;
import gitflow.GitFlowService;
import gitflow.util.GitFlowBranchUtil;
import gitflow.util.GitFlowConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class GitFlowPopupActions {

    private final Project myProject;
    private final GitRepository myRepository;
    private final GitFlowConfiguration gitFlowConfiguration;

    GitFlowPopupActions(@NotNull Project project, @NotNull GitRepository repository) {
        myProject = project;
        myRepository = repository;
        gitFlowConfiguration = GitFlowConfiguration.getInstance(project);
    }

    public ActionGroup createActions() {
        DefaultActionGroup popupGroup = new DefaultActionGroup(null, false);
        boolean gitflowInitialized = gitFlowConfiguration.isGitflowInitialized();

        popupGroup.addAction(new GitFlowInitAction(myProject, gitflowInitialized));

        if (!gitflowInitialized) {
            return popupGroup;
        }

        popupGroup.addSeparator("Feature");
        if (GitFlowBranchUtil.isCurrentBranchDevelop(myProject, myRepository)) {
            popupGroup.addAction(new GitFlowFeatureActions.StartFeatureAction(myProject, myRepository));
        }

        boolean isCurrentBranchFeature = GitFlowBranchUtil.isCurrentBranchFeature(myProject, myRepository);
        if (isCurrentBranchFeature) {
            if (GitFlowBranchUtil.isCurrentBranchNotPublished(myRepository)) {
                popupGroup.addAction(new GitFlowFeatureActions.PublishFeatureAction(myProject, myRepository));
            }
            popupGroup.addAction(new GitFlowFeatureActions.FinishFeatureAction(myProject, myRepository));
        }

        /*List<GitBranch> localBranches = new ArrayList<GitBranch>(myRepository.getBranches().getLocalBranches());
        Collections.sort(localBranches);
        for (GitBranch localBranch : localBranches) {
            if (!localBranch.equals(myRepository.getCurrentBranch())) { // don't show current branch in the list
                popupGroup.add(new GitFlowFeatureActions(myProject, localBranch.getName()));
            }
        }*/

        popupGroup.addSeparator("Hotfix");
        if (GitFlowBranchUtil.isCurrentBranchMaster(myProject, myRepository)) {
            popupGroup.addAction(new GitFlowHotfixActions.StartHotfixAction(myProject, myRepository));
        }

        boolean isCurrentBranchHotfix = GitFlowBranchUtil.isCurrentBranchHotfix(myProject, myRepository);
        if (isCurrentBranchHotfix) {
            if (GitFlowBranchUtil.isCurrentBranchNotPublished(myRepository)) {
                popupGroup.addAction(new GitFlowHotfixActions.PublishHotfixAction(myProject, myRepository));
            }
            popupGroup.addAction(new GitFlowHotfixActions.FinishHotfixAction(myProject, myRepository));
        }

        return popupGroup;
    }

    static class GitFlowInitAction extends DumbAwareAction {

        private final Project myProject;
        private final GitRepository myRepository;
        private final boolean forceReinitialization;

        public GitFlowInitAction(@NotNull Project myProject, boolean forceReinitialization) {
            super("Init");
            this.forceReinitialization = forceReinitialization;
            if (this.forceReinitialization) {
                this.getTemplatePresentation().setText("Init (force)");
            }
            this.myProject = myProject;
            this.myRepository = GitBranchUtil.getCurrentRepository(myProject);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            final GitFlowService gitflow = GitFlowService.getInstance(myProject);
            gitflow.initRepo(myRepository, forceReinitialization);
        }
    }

    static class GitFlowFeatureActions extends ActionGroup {

        private final Project project;
        private final GitRepository myRepository;
        private final String myBranchName;

        GitFlowFeatureActions(@NotNull Project project, @NotNull String branchName) {
            super("", true);
            this.project = project;
            this.myRepository = GitBranchUtil.getCurrentRepository(project);
            this.myBranchName = branchName;
            getTemplatePresentation().setText(calcBranchText(), false); // no mnemonics
            //getTemplatePresentation().setText("Finish feature: " + branchName, false); // no mnemonics
        }

        @NotNull
        private String calcBranchText() {
            String trackedBranch = new GitMultiRootBranchConfig(Collections.singletonList(myRepository)).getTrackedBranch(myBranchName);
            if (trackedBranch != null) {
                return myBranchName + " -> " + trackedBranch;
            } else {
                return myBranchName;
            }
        }

        @NotNull
        @Override
        public AnAction[] getChildren(@Nullable AnActionEvent e) {
            return new AnAction[]{
                    new FinishFeatureAction(project, myRepository)};/*,
                    new PublishHotfixAction(),
                    new TrackFeatureAction();
        }*/
        }

        private static class StartFeatureAction extends DumbAwareAction {
            private final Project myProject;
            private final GitRepository myRepository;

            public StartFeatureAction(@NotNull Project project, @NotNull GitRepository repository) {
                super("Start Feature");
                this.myProject = project;
                this.myRepository = repository;
            }

            @Override
            public void actionPerformed(AnActionEvent e) {
                // TODO: Handle nulls and weird values here
                final String newFeatureName = Messages.showInputDialog(myProject, "Enter the name of new feature:", "New Feature", Messages.getQuestionIcon(), "",
                        GitNewBranchNameValidator.newInstance(Collections.singletonList(myRepository)));
                if (newFeatureName != null) {
                    final GitFlowService gitflow = GitFlowService.getInstance(myProject);
                    gitflow.startFeature(myRepository, newFeatureName);
                }
            }

        }

        private static class PublishFeatureAction extends DumbAwareAction {
            private final Project myProject;
            private final GitRepository myRepository;

            public PublishFeatureAction(@NotNull Project project, @NotNull GitRepository repository) {
                super("Publish Feature");
                //getTemplatePresentation().setText("Finish feature: " + branchName, false); // no mnemonics
                this.myProject = project;
                this.myRepository = repository;
            }

            @Override
            public void actionPerformed(AnActionEvent e) {
                String currentFeatureName = GitFlowBranchUtil.getCurrentFeatureName(myProject, myRepository);
                final GitFlowService gitflow = GitFlowService.getInstance(myProject);
                gitflow.publishFeature(myRepository, currentFeatureName);
            }

        }

        private static class FinishFeatureAction extends DumbAwareAction {
            private final Project myProject;
            private final GitRepository myRepository;

            public FinishFeatureAction(@NotNull Project project, @NotNull GitRepository repository) {
                super("Finish Feature");
                //getTemplatePresentation().setText("Finish feature: " + branchName, false); // no mnemonics
                this.myProject = project;
                this.myRepository = repository;
            }

            @Override
            public void actionPerformed(AnActionEvent e) {
                String currentFeatureName = GitFlowBranchUtil.getCurrentFeatureName(myProject, myRepository);
                final GitFlowService gitflow = GitFlowService.getInstance(myProject);
                gitflow.finishFeature(myRepository, currentFeatureName);
            }
        }

    }


    private static class GitFlowHotfixActions extends ActionGroup {

        private final Project myProject;
        private final GitRepository myRepository;

        GitFlowHotfixActions(@NotNull Project project) {
            super("", true);
            this.myProject = project;
            this.myRepository = GitBranchUtil.getCurrentRepository(project);
            getTemplatePresentation().setText("Finish hotfix", false); // no mnemonics
        }

        @NotNull
        @Override
        public AnAction[] getChildren(@Nullable AnActionEvent e) {
            return new AnAction[]{
                    new FinishHotfixAction(myProject, myRepository)};/*,
                    new PublishHotfixAction(),
                    new TrackFeatureAction();
        }*/
        }

        private static class StartHotfixAction extends DumbAwareAction {

            private final Project myProject;
            private final GitRepository myRepository;

            public StartHotfixAction(@NotNull Project project, @NotNull GitRepository repository) {
                super("Start Hotfix");
                this.myProject = project;
                this.myRepository = repository;
            }

            @Override
            public void actionPerformed(AnActionEvent e) {
                final String newHotfixName = Messages.showInputDialog(myProject, "Enter the name of new hotfix:", "New Hotfix", Messages.getQuestionIcon(), "",
                        GitNewBranchNameValidator.newInstance(Collections.singletonList(myRepository)));
                if (newHotfixName != null) {
                    final GitFlowService gitflow = GitFlowService.getInstance(myProject);
                    gitflow.startHotfix(myRepository, newHotfixName);
                }
            }

        }

        private static class PublishHotfixAction extends DumbAwareAction {

            private final Project myProject;
            private final GitRepository myRepository;

            public PublishHotfixAction(@NotNull Project project, @NotNull GitRepository repository) {
                super("Publish Hotfix");
                this.myProject = project;
                this.myRepository = repository;
            }

            @Override
            public void actionPerformed(AnActionEvent e) {
                String currentHotfixName = GitFlowBranchUtil.getCurrentHotfixName(myProject, myRepository);
                final GitFlowService gitflow = GitFlowService.getInstance(myProject);
                gitflow.publishHotfix(myRepository, currentHotfixName);
            }

        }

        private static class FinishHotfixAction extends DumbAwareAction {

            private final Project myProject;
            private final GitRepository myRepository;

            public FinishHotfixAction(@NotNull Project project, @NotNull GitRepository repository) {
                super("Finish Hotfix");
                this.myProject = project;
                this.myRepository = repository;
            }

            @Override
            public void actionPerformed(AnActionEvent e) {
                String currentHotfixName = GitFlowBranchUtil.getCurrentHotfixName(myProject, myRepository);
                final GitFlowService gitflow = GitFlowService.getInstance(myProject);
                gitflow.finishHotfix(myRepository, currentHotfixName);
            }
        }

    }

}
