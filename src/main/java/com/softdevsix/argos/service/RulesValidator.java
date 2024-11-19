package com.softdevsix.argos.service;

import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.CodeSmells;
import com.softdevsix.argos.domain.Coverage;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.exception.*;
import org.springframework.stereotype.Component;

@Component
public class RulesValidator {

  public Rules validate(RulesRequestMap rulesRequestMap){
    Rules.RulesBuilder builder = Rules.builder();

    validateAndSetCodeQuality(rulesRequestMap, builder);
    validateAndSetBestPractices(rulesRequestMap, builder);
    validateAndSetCodeSmells(rulesRequestMap, builder);
    validateAndSetCodeComplexity(rulesRequestMap, builder);
    validateAndSetCodingStandards(rulesRequestMap, builder);
    validateAndSetCoverage(rulesRequestMap, builder);

    return builder.build();
  }

  private void validateAndSetCodeQuality(RulesRequestMap rulesRequestMap, Rules.RulesBuilder builder) {
        if (rulesRequestMap.getCodeQuality() != null) {
            CodeQuality codeQuality = rulesRequestMap.getCodeQuality();
            if (codeQuality.isMaxLineLengthEnabled() && codeQuality.getMaxLineLengthLimit() <= 0) {
                throw new LineLengthException("The line length limit must be positive.");
            }
            builder.codeQuality(codeQuality);
        }
    }

    private void validateAndSetBestPractices(RulesRequestMap rulesRequestMap, Rules.RulesBuilder builder) {
        if (rulesRequestMap.getBestPractices() != null) {
            builder.bestPractices(rulesRequestMap.getBestPractices());
        }
    }

    private void validateAndSetCodeSmells(RulesRequestMap rulesRequestMap, Rules.RulesBuilder builder) {
        if (rulesRequestMap.getCodeSmells() != null) {
            CodeSmells codeSmells = rulesRequestMap.getCodeSmells();
            if (codeSmells.isMethodTooLongEnabled() && codeSmells.getMaxMethodLength() <= 0) {
                throw new LineLengthException("The line length limit must be positive.");
            }
            builder.codeSmells(codeSmells);
        }
    }

    private void validateAndSetCodeComplexity(RulesRequestMap rulesRequestMap, Rules.RulesBuilder builder) {
        if (rulesRequestMap.getCodeComplexity() != null) {
            CodeComplexity codeComplexity = rulesRequestMap.getCodeComplexity();
            validateCyclomaticComplexity(codeComplexity);
            validateNestingDepth(codeComplexity);
            builder.codeComplexity(codeComplexity);
        }
    }

    private void validateCyclomaticComplexity(CodeComplexity codeComplexity) {
        if (codeComplexity.isCyclomaticComplexityLimitEnabled() && codeComplexity.getMaxCyclomaticComplexity() <= 0) {
            throw new CyclomaticComplexityException("The cyclomatic complexity limit must be positive.");
        }
    }

    private void validateNestingDepth(CodeComplexity codeComplexity) {
        if (codeComplexity.isNestingDepthLimitEnabled() && codeComplexity.getMaxNestingDepth() <= 0) {
            throw new NestingDepthException("The nesting depth limit must be positive.");
        }
    }

    private void validateAndSetCodingStandards(RulesRequestMap rulesRequestMap, Rules.RulesBuilder builder) {
        if (rulesRequestMap.getCodingStandards() != null) {
            builder.codingStandards(rulesRequestMap.getCodingStandards());
        }
    }

    private void validateAndSetCoverage(RulesRequestMap rulesRequestMap, Rules.RulesBuilder builder) {
        if (rulesRequestMap.getCoverage() != null) {
            Coverage coverage = rulesRequestMap.getCoverage();
            if (coverage.isMinCoveragePercentageEnabled() && coverage.getCoverageThreshold() < 0) {
                throw new CoverageThresholdException("The coverage threshold must be non-negative.");
            }
            builder.coverage(coverage);
        }
    }
}
