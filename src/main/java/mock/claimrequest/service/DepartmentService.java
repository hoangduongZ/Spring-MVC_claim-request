package mock.claimrequest.service;

import mock.claimrequest.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mock.claimrequest.dto.department.DepartmentDTO;
import mock.claimrequest.dto.department.DepartmentSaveDTO;

import java.util.List;

public interface DepartmentService {
    Page<DepartmentDTO> findAll(String keyword, Pageable pageable);

    void create(DepartmentSaveDTO departmentSaveDTO);

    List<Department> getDepartments();
    
    

}
