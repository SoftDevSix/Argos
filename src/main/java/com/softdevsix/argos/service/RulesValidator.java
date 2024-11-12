package com.softdevsix.argos.service;

import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.CodeSmells;
import com.softdevsix.argos.domain.Coverage;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.exception.RulesValidatorException;
import org.springframework.stereotype.Component;

@Component
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

  private Rules validateAndSetCodeQuality(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCodeQuality() != null) {
      CodeQuality codeQuality = rulesRequestMap.getCodeQuality();
      if (codeQuality.isMaxLineLengthEnabled() && codeQuality.getMaxLineLengthLimit() <= 0) {
        throw RulesValidatorException.LineLengthException();
      }
      validatedRules.setCodeQuality(codeQuality);
    }
    return validatedRules;
  }

  private Rules validateAndSetBestPractices(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getBestPractices() != null) {
      validatedRules.setBestPractices(rulesRequestMap.getBestPractices());
    }
    return validatedRules;
  }

  private Rules validateAndSetCodeSmells(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCodeSmells() != null) {
      CodeSmells codeSmells = rulesRequestMap.getCodeSmells();
      if (codeSmells.isMethodTooLongEnabled() && codeSmells.getMaxMethodLength() <= 0) {
        throw RulesValidatorException.LineLengthException();
      }
      validatedRules.setCodeSmells(codeSmells);
    }
    return validatedRules;
  }

  private Rules validateAndSetCodeComplexity(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCodeComplexity() != null) {
      CodeComplexity codeComplexity = rulesRequestMap.getCodeComplexity();
      validateCyclomaticComplexity(codeComplexity);
      validateNestingDepth(codeComplexity);
      validatedRules.setCodeComplexity(codeComplexity);
    }
    return validatedRules;
  }

  private void validateCyclomaticComplexity(CodeComplexity codeComplexity) {
    if (codeComplexity.isCyclomaticComplexityLimitEnabled() && codeComplexity.getMaxCyclomaticComplexity() <= 0) {
      throw RulesValidatorException.CyclomaticComplexityException();
    }
  }

  private void validateNestingDepth(CodeComplexity codeComplexity) {
    if (codeComplexity.isNestingDepthLimitEnabled() && codeComplexity.getMaxNestingDepth() <= 0) {
      throw RulesValidatorException.NestingDepthException();
    }
  }

  private Rules validateAndSetCodingStandards(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCodingStandards() != null) {
      validatedRules.setCodingStandards(rulesRequestMap.getCodingStandards());
    }
    return validatedRules;
  }

  private Rules validateAndSetCoverage(RulesRequestMap rulesRequestMap, Rules validatedRules) {
    if (rulesRequestMap.getCoverage() != null) {
      Coverage coverage = rulesRequestMap.getCoverage();
      if (coverage.isMinCoveragePercentageEnabled() && coverage.getCoverageThreshold() < 0) {
        throw RulesValidatorException.CoverageThresholdException();
      }
      validatedRules.setCoverage(coverage);
    }
    return validatedRules;
  }
}
