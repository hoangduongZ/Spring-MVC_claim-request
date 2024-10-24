package mock.claimrequest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerAdvisor {
//    @ExceptionHandler(NoProjectForCurrentEmployee.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleNoProjectForCurrentEmployee(NoProjectForCurrentEmployee ex, Model model) {
//        model.addAttribute("warnMessage", ex.getMessage());
//        return "warn/warn";
//    }
}
