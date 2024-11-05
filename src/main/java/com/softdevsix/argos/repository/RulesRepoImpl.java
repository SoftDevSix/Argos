package com.softdevsix.argos.repository;

import com.softdevsix.argos.domain.Rules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RulesRepoImpl
 */
@Component
public class RulesRepoImpl implements RulesRepo {

  private final BestPracticesRepository bestPractices;
  private final CodeComplexityRepository codeComplexity;
  private final CodeQualityRepository codeQuality;
  private final CodeSmellsRepository codeSmells;
  private final CodingStandardsRepository codingStandards;
  private final CoverageRepository coverage;

  @Autowired
  public RulesRepoImpl(BestPracticesRepository bestPractices,
                       CodeComplexityRepository codeComplexity,
                       CodeQualityRepository codeQuality,
                       CodeSmellsRepository codeSmells,
                       CodingStandardsRepository codingStandards,
                       CoverageRepository coverage) {
    this.bestPractices = bestPractices;
    this.codeComplexity = codeComplexity;
    this.codeQuality = codeQuality;
    this.codeSmells = codeSmells;
    this.codingStandards = codingStandards;
    this.coverage = coverage;
  }

  @Override
  public void createRule(Rules rules) {
    bestPractices.save(rules.getBestPractices());
    codeComplexity.save(rules.getCodeComplexity());
    codeQuality.save(rules.getCodeQuality());
    codeSmells.save(rules.getCodeSmells());
    codingStandards.save(rules.getCodingStandards());
    coverage.save(rules.getCoverage());
  }

  @Override
  public Rules fetchRule(Integer repoId) {
    Rules rules = new Rules();
    bestPractices.findAll().forEach(item -> {
      if (repoId.equals(item.getRepositoryId())) {
        rules.setBestPractices(item);
      }
    });
    codeComplexity.findAll().forEach(item -> {
      if (repoId.equals(item.getRepositoryId())) {
        rules.setCodeComplexity(item);
      }
    });
    codeQuality.findAll().forEach(item -> {
      if (repoId.equals(item.getRepositoryId())) {
        rules.setCodeQuality(item);
      }
    });
    codeSmells.findAll().forEach(item -> {
      if (repoId.equals(item.getRepositoryId())) {
        rules.setCodeSmells(item);
      }
    });
    codingStandards.findAll().forEach(item -> {
      if (repoId.equals(item.getRepositoryId())) {
        rules.setCodingStandards(item);
      }
    });
    coverage.findAll().forEach(item -> {
      if (repoId.equals(item.getRepositoryId())) {
        rules.setCoverage(item);
      }
    });

    return rules;
  }
}
