package mock.claimrequest.entity.entityEnum;

public enum ProjectRole {
    PM("Project Manager"),
    DEV("Developer"),
    QA("Quality Assurance"),
    DESIGN("Designer"),
    BA("Business Analyst"),
    TL("Technical lead"),
    TESTER("Tester");

    private final String displayName;

    ProjectRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}