package mock.claimrequest.dto.claim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.entity.ClaimStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimGetDto {
    private String employeeName;
    private String projectName;
    private String requestReason;
    private ClaimStatus status;
    private String createdTime;
}
