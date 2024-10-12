package mock.claimrequest.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("paid")
public class PaidController {
    @GetMapping
    public String getPaidNonVerify(Model model){
        model.addAttribute("currentPage", "paid");
        return "paid/paid-non-verify";
    }
}
