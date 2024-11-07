package com.softdevsix.argos.repository;

import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.domain.ProjectRules;
import com.softdevsix.argos.domain.Rules;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** RulesRepoImpl */
@Component
public class RulesRepoImpl implements RulesRepo {

  private BestPracticesRepository bestPractices;
  private CodeComplexityRepository codeComplexity;
  private CodeQualityRepository codeQuality;
  private CodeSmellsRepository codeSmells;
  private CodingStandardsRepository codingStandards;
  private CoverageRepository coverage;
  private ProjectRulesRepository projectRulesRepository;

  @Autowired
  public RulesRepoImpl(
      BestPracticesRepository bestPractices,
      CodeComplexityRepository codeComplexity,
      CodeQualityRepository codeQuality,
      CodeSmellsRepository codeSmells,
      CodingStandardsRepository codingStandards,
      CoverageRepository coverage,
      ProjectRulesRepository projectRulesRepository) {

    this.bestPractices = bestPractices;
    this.codeComplexity = codeComplexity;
    this.codeQuality = codeQuality;
    this.codeSmells = codeSmells;
    this.codingStandards = codingStandards;
    this.coverage = coverage;
    this.projectRulesRepository = projectRulesRepository;
  }

  @Override
  public Integer createRule(Rules rules, Project project) {
    ProjectRules projectRules =
        new ProjectRules(
            project,
            rules.getBestPractices(),
            rules.getCodeComplexity(),
            rules.getCodeQuality(),
            rules.getCodeSmells(),
            rules.getCodingStandards(),
            rules.getCoverage());

    bestPractices.save(rules.getBestPractices());
    codeComplexity.save(rules.getCodeComplexity());
    codeQuality.save(rules.getCodeQuality());
    codeSmells.save(rules.getCodeSmells());
    codingStandards.save(rules.getCodingStandards());
    coverage.save(rules.getCoverage());

    ProjectRules savedProjecRules = projectRulesRepository.save(projectRules);
    return savedProjecRules.getId();
  }

  @Override
  public Optional<Rules> fetchRule(Integer repoId) {
    Optional<ProjectRules> optional = projectRulesRepository.findById(repoId);
    if (optional.isEmpty()) {
      return Optional.empty();
    }

    ProjectRules repositoryRules = optional.get();
    Rules rules = new Rules();
    rules.setBestPractices(repositoryRules.getBestPractices());
    rules.setCodeComplexity(repositoryRules.getCodeComplexity());
    rules.setCodeQuality(repositoryRules.getCodeQuality());
    rules.setCodeSmells(repositoryRules.getCodeSmells());
    rules.setCodingStandards(repositoryRules.getCodingStandards());
    rules.setCoverage(repositoryRules.getCoverage());
    return Optional.of(rules);
  }
}
