package com.softdevsix.argos.repository;

import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.domain.ProjectRules;
import com.softdevsix.argos.domain.Rules;
import java.util.Optional;
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
    ProjectRules projectRules = ProjectRules.builder()
        .project(project)
        .bestPractices(rules.getBestPractices())
        .codeComplexity(rules.getCodeComplexity())
        .codeQuality(rules.getCodeQuality())
        .codeSmells(rules.getCodeSmells())
        .codingStandards(rules.getCodingStandards())
        .coverage(rules.getCoverage())
        .build();

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

    Rules rules = Rules.builder()
        .bestPractices(repositoryRules.getBestPractices())
        .codeComplexity(repositoryRules.getCodeComplexity())
        .codeQuality(repositoryRules.getCodeQuality())
        .codeSmells(repositoryRules.getCodeSmells())
        .codingStandards(repositoryRules.getCodingStandards())
        .coverage(repositoryRules.getCoverage())
        .build();

    return Optional.of(rules);
  }
}
