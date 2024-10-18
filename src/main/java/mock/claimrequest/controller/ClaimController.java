package mock.claimrequest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javassist.expr.NewArray;
import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.test.ClaimDetailTestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import mock.claimrequest.dto.test.ClaimTestDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.service.ClaimService;


@Controller
@RequestMapping("claims")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/add")
    public String getCreate(){
        return "claim/create";
    }

    @GetMapping
    public String getClaims(Model model, @RequestParam(required = false) String active){
        model.addAttribute("currentPage", "claims");
        if(active == null || "payment".equals(active)){
            List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.APPROVE);
            model.addAttribute("claims",claims );
            model.addAttribute("active","payment");
        }else if ("paid".equals(active)){
            List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.PAID);
            model.addAttribute("claims",claims);
            model.addAttribute("active","paid");
        }
        return "claim/index";
    }

    @PostMapping("/{id}/paid")
    public String postClaimsPaid(RedirectAttributes attributes, @PathVariable UUID id) {
        claimService.paidClaim(id);
        return "redirect:/claims";
    }

    @GetMapping("/{id}/detail")
    public String getClaimDetail(Model model, @PathVariable UUID id) {
        model.addAttribute("claim",claimService.findById(id));
        return "claim/detail";
    }

    @GetMapping("pending")
    public String getClaims(Model model){
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.PENDING);
        model.addAttribute("claims",claims);
        model.addAttribute("active","pending");
        return "claim/pending";
    }

    @PostMapping("{id}/cancel")
    public String postClaimCancel(RedirectAttributes attributes, @PathVariable UUID id){
        claimService.cancelClaim(id);
        return "claim/pending";
    }


    @GetMapping("/claim-test")
    public String showSubmitForm(Model model) {
        ClaimTestDTO claimDTO = new ClaimTestDTO();
        claimDTO.setClaimDetails(new ArrayList<>()); // Khởi tạo danh sách

        // Thêm một ClaimDetailDTO vào danh sách
        claimDTO.getClaimDetails().add(new ClaimDetailTestDTO());

        model.addAttribute("claimDTO", claimDTO);
        return "claim/form_submit_test" ;
    }
    //test
    @PostMapping("/claim-test")
    public String submitClaimer(@ModelAttribute("claimDTO") ClaimTestDTO claimTestDTO) {
        claimService.submitClaim(claimTestDTO);
        return "claim/create" ;

    }

    @GetMapping("/approve")
    public String approveClaim(Model model){
        List<ClaimTestDTO> pendingClaims = claimService.getClaimByStatusTest(ClaimStatus.PENDING);
        List<ClaimTestDTO> approvedClaims = claimService.getClaimByStatusTest(ClaimStatus.APPROVE);
        List<ClaimTestDTO> returnedClaims = claimService.getClaimByStatusTest(ClaimStatus.RETURN);
        List<ClaimTestDTO> rejectedClaims = claimService.getClaimByStatusTest(ClaimStatus.REJECT);

        model.addAttribute("pendingClaims", pendingClaims);
        model.addAttribute("approvedClaims", approvedClaims);
        model.addAttribute("returnedClaims", returnedClaims);
        model.addAttribute("rejectedClaims", rejectedClaims);
//        model.addAttribute("currentStatus", currentStatus);
        return "claim/approve/approve";

    }



    @GetMapping("/detail")
    public String getClaimDetailTest(Model model, @RequestParam UUID id){
        ClaimTestDTO claim = claimService.findByIdTest(id);
        model.addAttribute("claims",claim);
        return "claim/approve/detail";

    }

    @PostMapping("/submit")
    public String submitClaim(@RequestParam UUID id) {
        claimService.submitClaimById(id);
        return "redirect:/claims/approve";

    }

}






