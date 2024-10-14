package mock.claimrequest.entity;

public enum ClaimStatus {
    PENDING("Pending"),
    APPROVE("Approved"),
    RETURN("Returned"),
    REJECT("Rejected"),
    PAID("Paid"),
    CANCEL("Cancel"),
    DRAFT("Draft");

    private final String displayName;

    ClaimStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
