package mock.claimrequest.dto.claim;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
