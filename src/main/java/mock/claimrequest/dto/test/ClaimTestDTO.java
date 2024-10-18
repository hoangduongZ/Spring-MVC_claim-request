package mock.claimrequest.dto.test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import mock.claimrequest.entity.entityEnum.ClaimStatus;


@Getter
@Setter
public class ClaimTestDTO {

    private UUID id;

    private String title;

    private String employeeName;

    private String projectName;

    private LocalDateTime date;
    private String requestReason;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    private long duration;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private UUID projectId;

    private UUID employeeId;
    private List<ClaimDetailTestDTO> claimDetails = new ArrayList<>();

    
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;

    public String getFormattedCreatedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return createdTime.format(formatter);
    }

    public long calculateTotalDuration() {
        return claimDetails.stream()
                .filter(detail -> detail.getStartTime() != null && detail.getEndTime() != null)
                .mapToLong(detail -> Duration.between(detail.getStartTime(), detail.getEndTime()).toHours())
                .sum();
    }


}
