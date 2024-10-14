package mock.claimrequest.service.impl;

import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.service.EmployeeProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeProjectServiceImpl implements EmployeeProjectService {
    private final EmployeeProjectRepository employeeProjectRepository;

    public EmployeeProjectServiceImpl(EmployeeProjectRepository employeeProjectRepository) {
        this.employeeProjectRepository = employeeProjectRepository;
    }

    @Override
    public List<EmployeeProject> getAll() {
        return employeeProjectRepository.findAll();
    }
}
