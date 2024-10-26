package mock.claimrequest.dto.claim;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Claim title is required.")
    @Size(min = 5, max = 100, message = "Claim title must be between 5 and 100 characters.")
    private String title;

    @NotBlank(message = "Request reason is required.")
    @Size(min = 10, max = 500, message = "Request reason must be between 10 and 500 characters.")
    private String requestReason;

    @NotBlank(message = "Duration is required.")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?$", message = "Duration must be a valid number (e.g., 2.5).")
    private String duration;
    private ProjectGetDTO projectGetDTO;
    List<ClaimDetailDTO> claimDetails;
}
