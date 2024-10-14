package mock.claimrequest.controller;

import lombok.Getter;
import mock.claimrequest.dto.claim.ClaimGetDto;
import mock.claimrequest.entity.ClaimStatus;
import mock.claimrequest.service.ClaimService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("paid")
public class PaidController {
    private final ClaimService claimService;

    public PaidController(ClaimService claimService) {
        this.claimService = claimService;
    }


}
