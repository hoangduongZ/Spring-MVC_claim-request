package mock.claimrequest.service;

import jakarta.validation.Valid;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;

import java.util.List;

public interface ProjectService {
    void create(@Valid ProjectSaveDTO projectSaveDTO);
    List<ProjectDTO> list();
}
