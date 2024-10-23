package mock.claimrequest.controller;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.dto.claim.ClaimUpdateStatusDTO;
import mock.claimrequest.dto.project.ProjectGetDTO;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.entityEnum.AccountRole;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.service.ClaimService;
import mock.claimrequest.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("claims")
public class ClaimController {
    private final ClaimService claimService;
    private final ProjectService projectService;
    private final AuthService authService;

    public ClaimController(ClaimService claimService, ProjectService projectService, AuthService authService) {
        this.claimService = claimService;
        this.projectService = projectService;
        this.authService = authService;
    }

    @GetMapping("/add")
    public String showAddClaimForm(Model model) {
        Employee employee= authService.getCurrentAccount().getEmployee();
        ProjectGetDTO project= projectService.getCurrentProject(employee);
        if (project == null) {
            model.addAttribute("warnMessage", "No project found for the current employee. Please contact ADMIN to know information detail!");
            return "warn/warn";
        }
        ClaimSaveDTO claimSaveDTO = new ClaimSaveDTO();
        claimSaveDTO.setProjectGetDTO(project);
        model.addAttribute("claim", claimSaveDTO);
        return "claim/create";
    }

    @PostMapping("{status}/add")
    public String createClaim(@ModelAttribute("claim") ClaimSaveDTO claimSaveDTO, @PathVariable("status") String status) {
        switch (status.toLowerCase()) {
            case "draft":
            case "pending":
                claimService.actionCreate(ClaimStatus.valueOf(status.toUpperCase()), claimSaveDTO);
                if (status.equals("draft")) {
                    return "redirect:/claims/draft";
                }
                break;
            default:
                return "redirect:/claims/index";
        }
        return "redirect:/claims/index";
    }

    @PostMapping("{status}/{id}")
    public String updateClaimStatus(@ModelAttribute ClaimUpdateStatusDTO claimUpdateStatusDTO,
                              @PathVariable("status") String status,
                              @PathVariable("id") UUID id) {
        return switch (status.toLowerCase()) {
            case "approve" -> {
                claimService.updateStatus(ClaimStatus.APPROVE, id);
                yield "redirect:/claims/index?approve";
            }
            case "reject" -> {
                claimService.updateStatus(ClaimStatus.REJECT, id);
                yield "redirect:/claims/index?reject";
            }
            case "return" -> {
                claimService.updateStatus(ClaimStatus.RETURN, id);
                yield "redirect:/claims/index?return";
            }
            default -> "redirect:/claims/index";
        };
    }

    @GetMapping("/draft")
    public String getDraftClaims(Model model) {
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.DRAFT);
        model.addAttribute("currentPage", "draft");
        if (claims == null) {
            model.addAttribute("warnMessage", "You currently not in any project !");
            return "warn/warn";
        }
        model.addAttribute("claims", claims);
        return "claim/draft";
    }

    @GetMapping("/index{status}")
    public String getIndexClaim(Model model, @RequestParam(defaultValue = "pending",name = "status") String status) {
        ClaimStatus claimStatus = ClaimStatus.valueOf(status.toUpperCase());
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(claimStatus);
        model.addAttribute("currentPage", "claims");
        if (claims == null) {
            model.addAttribute("warnMessage", "You currently not in any project !");
            return "warn/warn";
        }
        model.addAttribute("claims", claims);

        AccountRole currentRole = authService.getCurrentRoleAccount();
        if (!Objects.equals(currentRole,AccountRole.ADMIN)) {
            claimService.getEmployeeRoleInProject().ifPresent(role -> {
                String roleName = role == ProjectRole.PM ? "pm" : "normal";
                model.addAttribute("role", roleName);
            });
        }
        model.addAttribute("active", status.toLowerCase());
        return "claim/index";
    }

    @GetMapping("/{id}/detail")
    public String getClaimDetail(Model model, @PathVariable UUID id) {
        model.addAttribute("claim", claimService.findById(id));
        return "claim/detail";
    }

    @GetMapping("/{id}/update")
    public String getUpdateClaim(Model model, @PathVariable UUID id){
        ClaimGetDTO claim = claimService.findById(id);
        model.addAttribute("claim",claim);
        return "claim/update";
    }

    @PostMapping("{status}/{id}/update")
    public String postUpdateClaim(@ModelAttribute ClaimGetDTO claim, @PathVariable("id") UUID id
            , @PathVariable("status") String status){
        claimService.update(claim, id, status);
        return "redirect:/claims/index";
    }


}
