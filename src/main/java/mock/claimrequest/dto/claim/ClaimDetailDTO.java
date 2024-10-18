package mock.claimrequest.dto.claim;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDetailDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
