package mock.claimrequest.controller;

import mock.claimrequest.service.ProjectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projects")
public class ProjectApiController {
    private final ProjectService projectService;

    public ProjectApiController(ProjectService projectService) {
        this.projectService = projectService;
    }

//    @GetMapping("/{id}/employees")
//    public ResponseEntity<?> getEmployeeProjects(@PathVariable("id") UUID projectId) {
//        return projectService.getEmployeeProjectsByProjectId(projectId);
//    }
}
