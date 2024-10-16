package mock.claimrequest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import mock.claimrequest.dto.employeeProject.EmployeeForProjectSaveDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import mock.claimrequest.service.EmployeeService;
import mock.claimrequest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("projects")
public class ProjectController {
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    public ProjectController(EmployeeService employeeService, ProjectService projectService) {
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @GetMapping("/add")
    public String getAddProject(Model model) throws JsonProcessingException {
        //TODO remove
//        ProjectSaveDTO  dto  = new ProjectSaveDTO();
//        EmployeeForProjectSaveDTO SaveDTO1 = new EmployeeForProjectSaveDTO();
//        SaveDTO1.setRole(ProjectRole.PM);
//        SaveDTO1.setEmployeeId(UUID.randomUUID());
//
//        List<EmployeeForProjectSaveDTO> employeeProjects = List.of(SaveDTO1);
//        dto.setEmployeeProjects(employeeProjects);
//        dto.setName("Abc");
//        dto.setBudget(new BigDecimal("10.5"));
//        dto.setStartDate(LocalDate.now());

        Object employees = employeeService.getAll();

        model.addAttribute("project", new ProjectSaveDTO());
        model.addAttribute("employees", employees);
        model.addAttribute("roles", ProjectRole.values());
        model.addAttribute("projectStatuses", ProjectStatus.values());
        String json = objectMapper.writeValueAsString(employees);

        model.addAttribute("employeesJSON", json);
        return "project/create";       
    }

    @PostMapping("/add")
    public String postAddProject(@ModelAttribute @Valid ProjectSaveDTO projectSaveDTO){
        projectService.create(projectSaveDTO);
        return "redirect:/projects/add";
    }
}
