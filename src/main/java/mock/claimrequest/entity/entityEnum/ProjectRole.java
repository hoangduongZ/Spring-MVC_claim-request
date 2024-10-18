package mock.claimrequest.entity.entityEnum;

public enum ProjectRole {
    PM("Project Manager", 50),
    DEV("Developer", 40),
    QA("Quality Assurance", 35),
    DESIGN("Designer", 35),
    BA("Business Analyst", 42),
    TL("Technical Lead", 45),
    TESTER("Tester", 30);
    private final String name;
    private final double salary;

    ProjectRole(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}