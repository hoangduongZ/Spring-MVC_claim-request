package mock.claimrequest.dto.test;

import java.time.LocalDateTime;
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

    private String title;


    private LocalDateTime date;
    private String requestReason;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    private long duration;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private UUID projectId;

    private UUID employeeId;

    
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;

//    EmployeeForProjectSaveDto employeeForProjectSaveDto ;
}
