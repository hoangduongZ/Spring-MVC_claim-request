package mock.claimrequest.service;


import jakarta.validation.Valid;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.dto.project.ProjectGetDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.ProjectStatus;

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

    List<ProjectGetDTO> getProjectForClaim(UUID id);

    void actionByStatus(ClaimStatus claimStatus, ClaimSaveDTO claimSaveDTO);
}
