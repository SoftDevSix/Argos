package com.softdevsix.argos.domain;

import lombok.Builder;
import lombok.Generated;
import lombok.Value;

@Generated
@Value
@Builder
public class Rules {
  private BestPracticesRules bestPractices;
  private CodeComplexityRules codeComplexity;
  private CodeQualityRules codeQuality;
  private CodeSmellsRules codeSmells;
  private CodingStandardsRules codingStandards;
  private CoverageRules coverage;
}
