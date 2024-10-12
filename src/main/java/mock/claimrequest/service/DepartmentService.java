package mock.claimrequest.service;

import mock.claimrequest.dto.department.DepartmentDTO;
import mock.claimrequest.dto.department.DepartmentSaveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    Page<DepartmentDTO> findAll(String keyword, Pageable pageable);

    void create(DepartmentSaveDTO departmentSaveDTO);
}
