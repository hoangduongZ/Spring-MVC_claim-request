package mock.claimrequest.controller;

import mock.claimrequest.dto.claim.ClaimEmailRequestDTO;
import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.dto.claim.ClaimUpdateStatusDTO;
import mock.claimrequest.dto.project.ProjectGetDTO;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.service.ClaimService;
import mock.claimrequest.service.EmailService;
import mock.claimrequest.service.EmployeeProjectService;
import mock.claimrequest.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("claims")
public class ClaimController {
    private final ClaimService claimService;
    private final ProjectService projectService;
    private final AuthService authService;
    private final EmployeeProjectService employeeProjectService;
    private final EmailService emailService;

    public ClaimController(ClaimService claimService, ProjectService projectService, AuthService authService, EmployeeProjectService employeeProjectService, EmailService emailService) {
        this.claimService = claimService;
        this.projectService = projectService;
        this.authService = authService;
        this.employeeProjectService = employeeProjectService;
        this.emailService = emailService;
    }

    @GetMapping("/add")
    public String showAddClaimForm(Model model) {
        Employee employee = authService.getCurrentAccount().getEmployee();
        ProjectGetDTO project = projectService.getCurrentProject(employee);

        String warningMessage = getWarningMessageIfAny(employee, project);
        if (warningMessage != null) {
            model.addAttribute("warnMessage", warningMessage);
            return "warn/warn";
        }

        ClaimSaveDTO claimSaveDTO = new ClaimSaveDTO();
        claimSaveDTO.setProjectGetDTO(project);
        model.addAttribute("claim", claimSaveDTO);
        return "claim/create";
    }

    private String getWarningMessageIfAny(Employee employee, ProjectGetDTO project) {
        if (project == null) {
            return "No project found for the current employee. Please contact ADMIN for details!";
        }

        ProjectRole role = employeeProjectService.getRoleInProject(employee.getId(), project.getId());
        if (role == ProjectRole.PM) {
            return "You are the <strong>PM</strong> of <strong>" + project.getName() + "</strong> and cannot create a claim!";
        }

        return null;
    }

    @PostMapping("{status}/add")
    public String createClaim(@ModelAttribute("claim") ClaimSaveDTO claimSaveDTO, @PathVariable("status") String status) throws IOException {
        switch (status.toLowerCase()) {
            case "draft":
                claimService.actionCreate(ClaimStatus.valueOf(status.toUpperCase()), claimSaveDTO);
                return "redirect:/claims/index/draft";
            case "pending":
                Claim claim = claimService.actionCreate(ClaimStatus.valueOf(status.toUpperCase()), claimSaveDTO);
                Project project = claim.getProject();
                Employee staff = claim.getEmployee();

                Employee pm = employeeProjectService.findProjectManager(project.getId());

                if (pm != null) {
                    String claimLink = "http://localhost:8080/claims/"+ claim.getId().toString() +"/detail";
                    ClaimEmailRequestDTO emailRequest = new ClaimEmailRequestDTO(
                            pm.getFirstname() + " " + pm.getLastname(),
                            pm.getAccount().getEmail(),
                            project.getName(),
                            staff.getFirstname() + " " + staff.getLastname(),
                            staff.getAccount().getEmail(),
                            staff.getId().toString(),
                            claimLink
                    );
                    emailService.sendClaimRequestEmail(emailRequest);
                }
                return "redirect:/claims/index/pending";
        }
        return "redirect:/claims/index/pending";
    }

    @PostMapping("{status}/{id}")
    public String updateClaimStatus(@ModelAttribute ClaimUpdateStatusDTO claimUpdateStatusDTO,
                                    @PathVariable("status") String status,
                                    @PathVariable("id") UUID id) {
        return switch (status.toLowerCase()) {
            case "approved" -> {
                claimService.updateStatus(ClaimStatus.APPROVED, id, claimUpdateStatusDTO);
                yield "redirect:/claims/index/approved";
            }
            case "paid" -> {
                claimService.updateStatus(ClaimStatus.PAID, id, claimUpdateStatusDTO);
                yield "redirect:/claims/index/paid";
            }
            case "canceled" -> {
                claimService.updateStatus(ClaimStatus.CANCELED, id, claimUpdateStatusDTO);
                yield "redirect:/claims/index/canceled";
            }
            case "rejected" -> {
                claimService.updateStatus(ClaimStatus.REJECTED, id, claimUpdateStatusDTO);
                yield "redirect:/claims/index/rejected";
            }
            case "returned" -> {
                claimService.updateStatus(ClaimStatus.DRAFT, id, claimUpdateStatusDTO);
                yield "redirect:/claims/index/pending";
            }
            default -> "redirect:/claims/index";
        };
    }

    @GetMapping("/index/{status}")
    public String getIndexClaim(
            Model model,
            @PathVariable(name = "status") String status,
            @RequestParam(defaultValue = "", name = "keyword") String keyword,
            @RequestParam(defaultValue = "", name = "sortOption") String sortOption,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "updatedTime", direction = Sort.Direction.DESC) Pageable pageable) {

        ClaimStatus claimStatus = ClaimStatus.valueOf(status.toUpperCase());

        Page<ClaimGetDTO> claimPage = claimService.getClaimByStatusAndKeyword(claimStatus, keyword, startDate, endDate, pageable);

        model.addAttribute("currentPage", "claims");
//    if (claimPage.isEmpty()) {
//        model.addAttribute("warnMessage", "You currently not in any project!");
//        return "warn/warn";
//    }

        model.addAttribute("claims", claimPage.getContent());
        model.addAttribute("totalPages", claimPage.getTotalPages());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("pageSize", pageable.getPageSize());
        model.addAttribute("sortField", pageable.getSort().toString());
        model.addAttribute("keyword", keyword);
        model.addAttribute("active", status);
        model.addAttribute("sortOption", sortOption);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return claimStatus == ClaimStatus.DRAFT ? "claim/draft" : "claim/index";
    }


    @GetMapping("/{id}/detail")
    public String getClaimDetail(Model model, @PathVariable UUID id) {
        model.addAttribute("claim", claimService.findById(id));
        return "claim/detail";
    }

    @GetMapping("/{id}/update")
    public String getUpdateClaim(Model model, @PathVariable UUID id) {
        ClaimGetDTO claim = claimService.findById(id);
        model.addAttribute("claim", claim);
        return "claim/update";
    }

    @PostMapping("{status}/{id}/update")
    public String postUpdateClaim(@ModelAttribute ClaimGetDTO claim, @PathVariable("id") UUID id
            , @PathVariable("status") String status) {
        claimService.update(claim, id, status);
        if (status.equalsIgnoreCase("draft")) {
            return "redirect:/claims/index/draft";
        }
        return "redirect:/claims/index/pending";
    }


}
