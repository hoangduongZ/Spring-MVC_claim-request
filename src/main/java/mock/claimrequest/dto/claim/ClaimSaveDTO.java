package mock.claimrequest.dto.claim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.dto.project.ProjectGetDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimSaveDTO {
    private String title;
    private String requestReason;
    private String duration;
    List<ClaimDetailDTO> claimDetailDTOS;
}
