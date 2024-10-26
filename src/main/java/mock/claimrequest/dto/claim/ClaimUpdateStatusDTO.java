package mock.claimrequest.dto.claim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.entity.entityEnum.ClaimStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimUpdateStatusDTO {
    private UUID id;
    private String status;
    private String requestReason;
    private String rejectReason;
    private String returnReason;
}
