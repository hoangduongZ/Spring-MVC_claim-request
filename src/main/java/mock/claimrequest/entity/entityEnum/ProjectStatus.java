package mock.claimrequest.entity.entityEnum;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ON_HOLD("On Hold"),
    CANCELLED("Cancelled");

    private final String name;

    ProjectStatus(String name) {
        this.name = name;
    }
}
