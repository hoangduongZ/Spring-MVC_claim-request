package mock.claimrequest.service;

import mock.claimrequest.dto.auth.AccountRegisterDTO;

import java.util.UUID;

public interface AccountService {
    boolean register(AccountRegisterDTO accountRegisterDTO);

    boolean existByEmail(String email);
    boolean existByUsername(String username);

    String  findEmailByEmployeeId(UUID id);
}
