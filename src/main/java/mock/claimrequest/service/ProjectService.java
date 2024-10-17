package mock.claimrequest.service;

import mock.claimrequest.entity.Project;
import mock.claimrequest.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByStatus(String projectStatus) {
        return projectRepository.findByProjectStatus(projectStatus);
    }
}
