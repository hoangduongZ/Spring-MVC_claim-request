package mock.claimrequest.service;


import jakarta.validation.Valid;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    void create(@Valid ProjectSaveDTO projectSaveDTO);
    List<ProjectDTO> list();
    List<ProjectDTO> listByInProgressStatus(ProjectStatus projectStatus);
    ProjectDTO getById(UUID projectId);

    List<EmployeeProjectDTO> getEmployeeProjectsByProjectId(UUID projectId);

    void update(@Valid ProjectDTO projectDTO);

    void delete(UUID id);
}
