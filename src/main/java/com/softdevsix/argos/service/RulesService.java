package com.softdevsix.argos.service;

import java.security.InvalidParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.repository.ProjectRepository;
import com.softdevsix.argos.repository.RulesRepoImpl;

@Component
public class RulesService {

  private RulesRepoImpl rulesRepo;
  private ProjectValidator projectValidator;
  private RulesValidator rulesValidator;

  @Autowired
  public RulesService(
      RulesRepoImpl rulesRepo,
      ProjectRepository projectRepository,
      ProjectValidator projectValidator) {
        this.rulesRepo = rulesRepo;
        this.projectValidator = projectValidator;
        this.rulesValidator = new RulesValidator();
  }

  public void handleRules(RulesRequestMap rulesRequestMap, Integer projectId) {
    Rules validatedRules = rulesValidator.validate(rulesRequestMap);
    Project project = projectValidator.validate(projectId);
    rulesRepo.createRule(validatedRules, project);
  }

  public Rules getRules(Integer projectId) {
    return rulesRepo
        .fetchRule(projectId)
        .orElseGet(
            () -> {
              throw new InvalidParameterException("Could not find requested project");
            });
  }
}
