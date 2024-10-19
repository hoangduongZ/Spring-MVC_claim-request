package mock.claimrequest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.dto.test.ClaimDetailTestDTO;
import mock.claimrequest.dto.test.ClaimTestDTO;
import mock.claimrequest.entity.Claim;
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
    public String showAddClaimForm(Model model) {
        model.addAttribute("claim", new ClaimSaveDTO());
        return "claim/create";
    }

    @PostMapping("/claims/add")
    public String addClaim(@ModelAttribute Claim claim) {
        return "redirect:/claims";
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

        model.addAttribute("claimDTO", claimDTO);
        return "claim/form_submit_test" ;
    }
    //test
    @PostMapping("/claim-test")
    public String submitClaimer(@ModelAttribute("claimDTO") ClaimTestDTO claimTestDTO, Model model) {
        if (claimTestDTO.getClaimDetails() == null || claimTestDTO.getClaimDetails().isEmpty()) {
            model.addAttribute("error", "Claim details must not be empty!");
            return "claim/form_submit_test"; // Quay lại trang nhập liệu với thông báo lỗi
        }
        else {
            try {
                claimService.submitClaim(claimTestDTO);
            } catch (Exception e) {
                model.addAttribute("error", "There was an error submitting your claim: " + e.getMessage());
                return "redirect:/claims/claim-test"; // Trả lại trang form để người dùng có thể sửa chữa
            }
            return "claim/approve/approve";
        }

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


    @GetMapping("/reject")
    public String getClaimRejectTest(Model model, @RequestParam UUID id){
        ClaimTestDTO claim = claimService.findByIdTest(id);
        model.addAttribute("claims",claim);
        return "claim/approve/reject";

    }

}






