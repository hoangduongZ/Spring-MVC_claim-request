package mock.claimrequest.dto.claim;

import mock.claimrequest.entity.entityEnum.ClaimStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClaimApproveDTO {

    private UUID id;
    private String employeeName;
    private String projectName;
    private String requestReason;
    private ClaimStatus status;
    private LocalDateTime createdTime;
}
