package mock.claimrequest.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    private boolean gender;
    private LocalDate dob;
    private String address;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Account accounts;

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeProject> employeeProjects = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private List<Claim> claims;
}