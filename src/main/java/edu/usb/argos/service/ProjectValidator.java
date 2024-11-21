package edu.usb.argos.argos.service;

import edu.usb.argos.argos.domain.Project;
import edu.usb.argos.argos.domain.ProjectRules;
import edu.usb.argos.argos.exception.ProjectNotFoundException;
import edu.usb.argos.argos.repository.ProjectRepository;
import edu.usb.argos.argos.repository.ProjectRulesRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {

  private final ProjectRepository projectRepository;
  private final ProjectRulesRepository projectRulesRepository;

  public ProjectValidator(
      ProjectRepository projectRepository, ProjectRulesRepository projectRulesRepository) {
    this.projectRepository = projectRepository;
    this.projectRulesRepository = projectRulesRepository;
  }

  public Project validate(Integer projectId) {
    validateProjectAvailability(projectId);
    return validateProjectExistence(projectId);
  }

  private void validateProjectAvailability(Integer projectId) {
    Optional<ProjectRules> optional = projectRulesRepository.findById(projectId);
    if (optional.isPresent()) {
        throw new IllegalArgumentException("Project already has rules");
    }
  }

  private Project validateProjectExistence(Integer projectId) {
    Optional<Project> optional = projectRepository.findById(projectId);
    if (optional.isEmpty()) {
        throw new ProjectNotFoundException("Provided project ID is not registered");
    }
    return optional.get();
  }
}
