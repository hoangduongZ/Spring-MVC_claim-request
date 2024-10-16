package mock.claimrequest.service;

import jakarta.validation.Valid;
import mock.claimrequest.dto.project.ProjectSaveDTO;

public interface ProjectService {
    void create(@Valid ProjectSaveDTO projectSaveDTO);
}
