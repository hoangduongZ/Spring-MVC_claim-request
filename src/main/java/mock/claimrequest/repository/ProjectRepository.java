package mock.claimrequest.repository;

import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByProjectStatus(String projectStatus);



}
