package mock.claimrequest.entity.entityEnum;

public enum ClaimStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    RETURNED("Returned"),
    REJECTED("Rejected"),
    PAID("Paid"),
    CANCELED("Cancel"),
    DRAFT("Draft");

    private final String displayName;

    ClaimStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
