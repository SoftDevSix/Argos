package com.softdevsix.argos.service;

import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.CodeSmells;
import com.softdevsix.argos.domain.Coverage;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.domain.RulesRequestMap;

public class RulesValidator {

  public Rules validate(RulesRequestMap rulesRequestMap){
    Rules rules = new Rules();
    validateAndSetCodeQuality(rulesRequestMap, rules );
    validateAndSetBestPractices(rulesRequestMap, rules );
    validateAndSetCodeSmells(rulesRequestMap, rules );
    validateAndSetCodeComplexity(rulesRequestMap, rules );
    validateAndSetCodingStandards(rulesRequestMap, rules );
    validateAndSetCoverage(rulesRequestMap, rules );
    return rules;
  }

  private void validateAndSetCodeQuality(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCodeQuality() != null) {
      CodeQuality codeQuality = rulesRequestMap.getCodeQuality();
      if (codeQuality.isMaxLineLengthEnabled() && codeQuality.getMaxLineLengthLimit() <= 0) {
        throw new IllegalArgumentException("The line length limit must be positive");
      }
      validatedRules.setCodeQuality(codeQuality);
    }
  }

  private void validateAndSetBestPractices(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getBestPractices() != null) {
      validatedRules.setBestPractices(rulesRequestMap.getBestPractices());
    }
  }

  private void validateAndSetCodeSmells(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCodeSmells() != null) {
      CodeSmells codeSmells = rulesRequestMap.getCodeSmells();
      if (codeSmells.isMethodTooLongEnabled() && codeSmells.getMaxMethodLength() <= 0) {
        throw new IllegalArgumentException("The method length limit must be positive");
      }
      validatedRules.setCodeSmells(codeSmells);
    }
  }

  private void validateAndSetCodeComplexity(RulesRequestMap rulesRequestMap, Rules validatedRules) {
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

  private void validateAndSetCodingStandards(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCodingStandards() != null) {
      validatedRules.setCodingStandards(rulesRequestMap.getCodingStandards());
    }
  }

  private void validateAndSetCoverage(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCoverage() != null) {
      Coverage coverage = rulesRequestMap.getCoverage();
      if (coverage.isMinCoveragePercentageEnabled() && coverage.getCoverageThreshold() < 0) {
        throw new IllegalArgumentException("The coverage threshold must be non-negative");
      }
      validatedRules.setCoverage(coverage);
    }
  }
}
