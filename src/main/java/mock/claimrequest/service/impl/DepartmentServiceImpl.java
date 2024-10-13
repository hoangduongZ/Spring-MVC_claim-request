package mock.claimrequest.service.impl;

import mock.claimrequest.dto.department.DepartmentDTO;
import mock.claimrequest.dto.department.DepartmentSaveDTO;
import mock.claimrequest.entity.Department;
import mock.claimrequest.repository.DepartmentRepository;
import mock.claimrequest.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private ModelMapper modelMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    public Page<DepartmentDTO> findAll(String keyword, Pageable pageable){
        Specification<Department> specification =(root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");
        };

        var departments = departmentRepository.findAll(specification, pageable);

        return departments.map(ingredient -> {
            var departmentDTO = new DepartmentDTO();
            departmentDTO.setId(ingredient.getId());
            departmentDTO.setName(ingredient.getName());
            return departmentDTO;
        });
    }

    @Override
    public void create(DepartmentSaveDTO departmentSaveDTO){
        if (departmentSaveDTO == null){
            throw new IllegalStateException("Department not null !");
        }
        Department department = modelMapper.map(departmentSaveDTO, Department.class);
        departmentRepository.save(department);
    }

}
