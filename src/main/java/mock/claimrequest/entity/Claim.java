package mock.claimrequest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "claim")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private LocalDateTime date;
    @Column(name = "request_reason")
    private String requestReason;
    @Column(name = "reject_reason")
    private String rejectReason;
    @Column(name = "return_reason")
    private String returnReason;
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;
    private long duration;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimDetail> claimDetails = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;
}
