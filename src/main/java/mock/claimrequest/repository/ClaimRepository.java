package mock.claimrequest.repository;

import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findAllByStatus(ClaimStatus claimStatus);

    List<Claim> findAllByStatusAndEmployee(ClaimStatus status, Employee employee);

    List<Claim> findAllByStatusAndProject(ClaimStatus claimStatus, Project project);

    @Query("SELECT c FROM Claim c JOIN FETCH c.employee e " +
            "WHERE c.status = :status AND (:employee IS NULL OR c.employee = :employee)")
    Page<Claim> findByStatusAndEmployee(
            @Param("status") ClaimStatus status,
            @Param("employee") Employee employee,
            Pageable pageable);

    @Query("SELECT c FROM Claim c " +
            "JOIN FETCH c.employee e " +
            "JOIN c.project p " +
            "JOIN c.claimDetails d " +  // Join ClaimDetail
            "WHERE c.status = :status " +
            "AND (c.title LIKE %:keyword% OR c.requestReason LIKE %:keyword%) " +
            "AND (:employee IS NULL OR c.employee = :employee) " +
            "AND (" +
            "   (:startDate IS NULL AND :endDate IS NULL) OR " +
            "   (d.startTime BETWEEN :startDate AND :endDate OR d.endTime BETWEEN :startDate AND :endDate OR " +
            "    (d.startTime <= :startDate AND d.endTime >= :endDate))" +
            ")")
    Page<Claim> findByStatusKeywordAndDateRange(
            @Param("status") ClaimStatus status,
            @Param("keyword") String keyword,
            @Param("employee") Employee employee,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    Page<Claim> findByStatus(ClaimStatus status, Pageable pageable);

    @Query("SELECT c FROM Claim c " +
            "JOIN FETCH c.employee e " +
            "JOIN c.project p " +
            "JOIN c.claimDetails d " +  // Join ClaimDetail
            "WHERE c.status = :status " +
            "AND (c.title LIKE %:keyword% OR e.account.userName LIKE %:keyword%) " +
            "AND p.id = :projectId " +
            "AND (" +
            "   (:startDate IS NULL AND :endDate IS NULL) OR " +
            "   (d.startTime BETWEEN :startDate AND :endDate OR d.endTime BETWEEN :startDate AND :endDate OR " +
            "    (d.startTime <= :startDate AND d.endTime >= :endDate))" +
            ")")
    Page<Claim> findByStatusKeywordAndDateRangeAndProjectId(
            @Param("status") ClaimStatus status,
            @Param("keyword") String keyword,
            @Param("projectId") UUID projectId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT c FROM Claim c JOIN FETCH c.employee e " +
            "JOIN c.project p " +
            "WHERE c.status = :status " +
            "AND p.id = :projectId")
    Page<Claim> findByStatusAndProject(
            @Param("status") ClaimStatus status,
            @Param("projectId") UUID projectId,
            Pageable pageable);
}
