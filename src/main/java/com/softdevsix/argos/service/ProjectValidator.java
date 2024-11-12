package com.softdevsix.argos.service;

import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.exception.ProjectNotFoundException;
import com.softdevsix.argos.repository.ProjectRepository;
import com.softdevsix.argos.repository.ProjectRulesRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {

  private final ProjectRepository projectRepository;
  private final ProjectRulesRepository projectRulesRepository;

  @Autowired
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
    projectRulesRepository
        .findAll()
        .forEach(
            projectRules -> {
              if (projectRules.getProject().getId().equals(projectId)) {
                throw new IllegalArgumentException("Project already has rules");
              }
            });
  }

  private Project validateProjectExistence(Integer projectId) {
    Optional<Project> optional = projectRepository.findById(projectId);
    if (optional.isEmpty()) {
        throw new ProjectNotFoundException("Provided project ID is not registered");
    }
    return optional.get();
  }
}
