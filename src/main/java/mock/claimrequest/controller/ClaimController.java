package mock.claimrequest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("claims")
public class ClaimController {
    @GetMapping("/add")
    public String getCreate(){
        return "claim/create";
    }
}
