package mock.claimrequest.controller;

import mock.claimrequest.service.ClaimService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/claims")
public class ClaimApiController {
    private final ClaimService claimService;

    public ClaimApiController(ClaimService claimService) {
        this.claimService = claimService;
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
}
