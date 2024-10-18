package mock.claimrequest.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import mock.claimrequest.entity.entityEnum.ProjectRole;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "employee_project")
public class EmployeeProject implements Serializable {
    @EmbeddedId
    private EmployeeProjectId id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId("employeeId")
    private Employee employee;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId("projectId")
    private Project project;

    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeProject)) return false;
        EmployeeProject that = (EmployeeProject) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
