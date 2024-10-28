package mock.claimrequest.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mock.claimrequest.entity.Account;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AccountInterceptor implements HandlerInterceptor {
    private final AccountService accountService;
    private final AuthService authService;

    public AccountInterceptor(AccountService accountService, AuthService authService) {
        this.accountService = accountService;
        this.authService = authService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            Account account = authService.getCurrentAccount();
            if (account != null) {
                modelAndView.addObject("account", account);
            } else {
                modelAndView.addObject("account", new Account());
            }
        }
    }
}
