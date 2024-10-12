package mock.claimrequest.entity;

public enum ProjectRole {
    PM("Project Manager"),
    DEV("Developer"),
    QA("Quality Assurance"),
    DESIGN("Designer"),
    BA("Business Analyst");

    private final String displayName;

    ProjectRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}