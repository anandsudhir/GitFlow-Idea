package gitflow.ui;

public class GitFlowConstants {

    public static final String GITFLOW = "gitflow";
    public static final String PREFIX = "prefix";
    public static final String DOT = ".";

    public static final String MASTER = "master";
    public static final String DEVELOP = "develop";

    public static final String BRANCH_MASTER = "gitflow.branch.master";
    public static final String BRANCH_DEVELOP = "gitflow.branch.develop";

    public static final String PREFIX_FEATURE = "gitflow.prefix.feature";
    public static final String PREFIX_RELEASE = "gitflow.prefix.release";
    public static final String PREFIX_HOTFIX = "gitflow.prefix.hotfix";
    public static final String PREFIX_SUPPORT = "gitflow.prefix.support";
    public static final String PREFIX_VERSIONTAG = "gitflow.prefix.versiontag";

    public static enum PREFIXES {
        FEATURE, RELEASE, HOTFIX, SUPPORT, VERSIONTAG;

        public String configKey() {
            return GITFLOW + DOT + PREFIX + DOT + name().toLowerCase();
        }
    }
}
