package com.softdevsix.argos.service;

import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.CodeSmells;
import com.softdevsix.argos.domain.Coverage;
import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.repository.ProjectRepository;
import com.softdevsix.argos.repository.ProjectRulesRepository;
import com.softdevsix.argos.repository.RulesRepoImpl;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RulesService {

  private Rules validatedRules;
  private RulesRepoImpl rulesRepo;
  private ProjectRepository projectRepository;
  private ProjectRulesRepository projectRulesRepository;

  @Autowired
  public RulesService(
      RulesRepoImpl rulesRepo,
      ProjectRepository projectRepository,
      ProjectRulesRepository projectRulesRepository) {
    this.validatedRules = new Rules();
    this.rulesRepo = rulesRepo;
    this.projectRepository = projectRepository;
    this.projectRulesRepository = projectRulesRepository;
  }

  public void handleRules(RulesRequestMap rulesRequestMap, Integer projectId) {
    validateAndSetCodeQuality(rulesRequestMap);
    validateAndSetBestPractices(rulesRequestMap);
    validateAndSetCodeSmells(rulesRequestMap);
    validateAndSetCodeComplexity(rulesRequestMap);
    validateAndSetCodingStandards(rulesRequestMap);
    validateAndSetCoverage(rulesRequestMap);
    validateProjectAvailability(projectId);
    Project project = validateProjectExitence(projectId);
    rulesRepo.createRule(validatedRules, project);
  }

  private void validateAndSetCodeQuality(RulesRequestMap rulesRequestMap) {
    if (rulesRequestMap.getCodeQuality() != null) {
      CodeQuality codeQuality = rulesRequestMap.getCodeQuality();
      if (codeQuality.isMaxLineLengthEnabled() && codeQuality.getMaxLineLengthLimit() <= 0) {
        throw new IllegalArgumentException("The line length limit must be positive");
      }
      validatedRules.setCodeQuality(codeQuality);
    }
  }

  private void validateAndSetBestPractices(RulesRequestMap rulesRequestMap) {
    if (rulesRequestMap.getBestPractices() != null) {
      validatedRules.setBestPractices(rulesRequestMap.getBestPractices());
    }
  }

  private void validateAndSetCodeSmells(RulesRequestMap rulesRequestMap) {
    if (rulesRequestMap.getCodeSmells() != null) {
      CodeSmells codeSmells = rulesRequestMap.getCodeSmells();
      if (codeSmells.isMethodTooLongEnabled() && codeSmells.getMaxMethodLength() <= 0) {
        throw new IllegalArgumentException("The method length limit must be positive");
      }
      validatedRules.setCodeSmells(codeSmells);
    }
  }

  private void validateAndSetCodeComplexity(RulesRequestMap rulesRequestMap) {
    if (rulesRequestMap.getCodeComplexity() != null) {
      CodeComplexity codeComplexity = rulesRequestMap.getCodeComplexity();
      if (codeComplexity.isCyclomaticComplexityLimitEnabled()
          && codeComplexity.getMaxCyclomaticComplexity() <= 0) {
        throw new IllegalArgumentException("The cyclomatic complexity limit must be positive");
      }
      if (codeComplexity.isNestingDepthLimitEnabled() && codeComplexity.getMaxNestingDepth() <= 0) {
        throw new IllegalArgumentException("The nesting depth limit must be positive.");
      }
      validatedRules.setCodeComplexity(codeComplexity);
    }
  }

  private void validateAndSetCodingStandards(RulesRequestMap rulesRequestMap) {
    if (rulesRequestMap.getCodingStandards() != null) {
      validatedRules.setCodingStandards(rulesRequestMap.getCodingStandards());
    }
  }

  private void validateAndSetCoverage(RulesRequestMap rulesRequestMap) {
    if (rulesRequestMap.getCoverage() != null) {
      Coverage coverage = rulesRequestMap.getCoverage();
      if (coverage.isMinCoveragePercentageEnabled() && coverage.getCoverageThreshold() < 0) {
        throw new IllegalArgumentException("The coverage threshold must be non-negative");
      }
      validatedRules.setCoverage(coverage);
    }
  }

  private void validateProjectAvailability(Integer projectId) {
    projectRulesRepository
        .findAll()
        .forEach(
            projectRules -> {
              if (projectRules.getProject().getId() == projectId) {
                throw new IllegalArgumentException("Project already has rules");
              }
            });
  }

  private Project validateProjectExitence(Integer projectId) {
    Optional<Project> optional = projectRepository.findById(projectId);
    if (optional.isEmpty()) {
      throw new IllegalArgumentException("Provided project ID is not registered");
    }

    return optional.get();
  }

  public Rules getRules() {
    return validatedRules;
  }
}
