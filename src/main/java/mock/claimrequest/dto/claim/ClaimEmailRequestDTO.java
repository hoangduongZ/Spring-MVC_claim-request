package mock.claimrequest.dto.claim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClaimEmailRequestDTO {
    private String pmName;
    private String emailPm;
    private String projectName;
    private String staffName;
    private String emailCC;
    private String staffId;
    private String claimLink;
}
