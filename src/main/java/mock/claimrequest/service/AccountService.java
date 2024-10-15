package mock.claimrequest.service;

import mock.claimrequest.dto.auth.AccountRegisterDTO;

public interface AccountService {
    boolean register(AccountRegisterDTO accountRegisterDTO);

    boolean existByEmail(String email);
    boolean existByUsername(String username);
}
