package com.softdevsix.argos.repository;

import com.softdevsix.argos.domain.Rules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RulesRepoImpl
 */
@Component
public class RulesRepoImpl implements RulesRepo {

  @Autowired private BestPracticesRepository bestPractices;
  @Autowired private CodeComplexityRepository codeComplexity;
  @Autowired private CodeQualityRepository codeQuality;
  @Autowired private CodeSmellsRepository codeSmells;
  @Autowired private CodingStandardsRepository codingStandards;
  @Autowired private CoverageRepository coverage;

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
      if (item.getRepositoryId() == repoId) {
        rules.setBestPractices(item);
      }
    });
    codeComplexity.findAll().forEach(item -> {
      if (item.getRepositoryId() == repoId) {
        rules.setCodeComplexity(item);
      }
    });
    codeQuality.findAll().forEach(item -> {
      if (item.getRepositoryId() == repoId) {
        rules.setCodeQuality(item);
      }
    });
    codeSmells.findAll().forEach(item -> {
      if (item.getRepositoryId() == repoId) {
        rules.setCodeSmells(item);
      }
    });
    codingStandards.findAll().forEach(item -> {
      if (item.getRepositoryId() == repoId) {
        rules.setCodingStandards(item);
      }
    });
    coverage.findAll().forEach(item -> {
      if (item.getRepositoryId() == repoId) {
        rules.setCoverage(item);
      }
    });

    return rules;
  }
}
