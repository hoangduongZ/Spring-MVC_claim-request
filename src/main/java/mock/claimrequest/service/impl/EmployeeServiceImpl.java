package mock.claimrequest.service.impl ;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mock.claimrequest.dto.employee.EmployeeDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Department;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.DepartmentRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.service.EmployeeService;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    

    
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllEmployees'");
    }

    @Override
    public Employee getEmployeeById(UUID id) {

        throw new UnsupportedOperationException("Unimplemented method 'getEmployeeByID'");
        // if (id == null) {

        //     throw new IllegalArgumentException("ID must not be null");
        // }

        // // Lấy Employee theo ID
        // return employeeRepository.findById(id)
        //         .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }


    @Override
    public void updateEmployee(UUID id, EmployeeDTO employeeDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEmployee'");
    }

    @Override
    public void deleteEmployee(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteEmployee'");
    }


     @Autowired
     private EmployeeRepository employeeRepository;

     @Autowired
     private AccountRepository accountRepository;

     @Autowired
     private DepartmentRepository departmentRepository;

    // @Override
    // public List<EmployeeDTO> getAllEmployees() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getAllEmployees'");
    // }

    // @Override
    // public Employee getEmployeeById(UUID id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getEmployeeById'");
    // }

    @Override
    public void saveEmployee(EmployeeDTO employeeDTO) {
       // tạo Employee từ DTO
        Employee employee = new Employee(); // tạo object
        employee.setFirstname(employeeDTO.getFirstname());   // ko có ràng buộc
        employee.setLastname(employeeDTO.getLastname());
        employee.setGender(employeeDTO.isGender());
        employee.setDob(employeeDTO.getDob());
        employee.setAddress(employeeDTO.getAddress());

        // tìm kiếm Department theo tên từ DepartmentDTO
        Department department = departmentRepository.findByName(employeeDTO.getDepartment().getName())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        employee.setDepartment(department); // gán Department cho Employee

        // lưu Employee
        Employee savedEmployee = employeeRepository.save(employee);

        // tao Account từ DTO, bắt lỗi ngoại l ệ
        Account account = new Account();
        account.setUserName(employeeDTO.getAccountDTO().getUserName()); //đoạn này bị lỗi, cần thêm unique
        account.setEmail(employeeDTO.getAccountDTO().getEmail());
        account.setPassword(employeeDTO.getAccountDTO().getPassword());
        account.setEmployee(savedEmployee); // gắn Employee vào Account

        accountRepository.save(account);
    }

    // @Override
    // public void updateEmployee(UUID id, EmployeeDTO employeeDTO) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'updateEmployee'");
    // }

    // @Override
    // public void deleteEmployee(UUID id) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteEmployee'");
    // }

    // @Override
    // public List<Department> getAllDepartments() {
    //     return departmentRepository.findAll();
    // }
    
}
