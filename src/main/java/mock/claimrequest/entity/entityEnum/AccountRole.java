package mock.claimrequest.entity.entityEnum;

import lombok.Getter;

@Getter
public enum AccountRole {
    ADMIN("Admin"),
    CLAIMER("Claimer"),
    FINANCE("Finance Officer"),
    APPROVER("Approver");

    private final String displayName;

    AccountRole(String displayName) {
        this.displayName = displayName;
    }

}

