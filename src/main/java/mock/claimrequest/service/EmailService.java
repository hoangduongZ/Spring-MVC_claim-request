package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimEmailRequestDTO;

import java.io.IOException;
import java.util.UUID;

public interface EmailService {
    void sendClaimRequestEmail(ClaimEmailRequestDTO claimEmailRequestDTO) throws IOException;

    void sendResetLink(String email);

}
