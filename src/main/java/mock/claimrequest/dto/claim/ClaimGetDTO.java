package mock.claimrequest.dto.claim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.entityEnum.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimGetDTO {
    private UUID id;
    private String title;
    private String employeeName;
    private String projectName;
    private String requestReason;
    private String returnReason;
    private String rejectReason;
    private ClaimStatus status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private BigDecimal amount;
    private ProjectDTO project;
    private Account account;
    private double duration;
    private List<ClaimDetailDTO> claimDetailDTOList;

    public String getFormattedCreatedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return createdTime.format(formatter);
    }

    public String getFormattedUpdatedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return updatedTime.format(formatter);
    }
}
