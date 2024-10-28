package mock.claimrequest.controller;

import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.service.ClaimService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/claims")
public class ClaimApiController {
    private final ClaimService claimService;
    private final ClaimRepository claimRepository;

    public ClaimApiController(ClaimService claimService, ClaimRepository claimRepository) {
        this.claimService = claimService;
        this.claimRepository = claimRepository;
    }

    @PostMapping("/export")
    public ResponseEntity<ByteArrayResource> exportClaims(@RequestParam List<UUID> claimIds) throws IOException {
        ByteArrayOutputStream outputStream = claimService.exportClaimsToExcel(claimIds);

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=claims.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/report/status")
    public Map<String, Long> reportStatus() {
        Long rejected = claimService.countByStatus(ClaimStatus.REJECTED);
        Long paid = claimService.countByStatus(ClaimStatus.PAID);
        Map<String, Long> rateMap= new HashMap<>();
        rateMap.put("paid",paid);
        rateMap.put("rejected",rejected);
        return rateMap;
    }

    @GetMapping("/report/project")
    public Map<String, Long> reportPerProjectRequest() {
        return claimRepository.findAll().stream()
                .collect(Collectors.groupingBy(claim -> claim.getProject().getName(), Collectors.counting()));
    }

    @GetMapping("/report/by-date")
    public ResponseEntity<Map<LocalDate, Long>> reportClaimsByDate() {
        Map<LocalDate, Long> claimCounts = claimService.countClaimsByDate();
        return ResponseEntity.ok(claimCounts);
    }
}
