package com.softdevsix.argos.domain;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class RulesRequestMap {
  private CodeQualityRules codeQuality;
  private BestPracticesRules bestPractices;
  private CodeSmellsRules codeSmells;
  private CodeComplexityRules codeComplexity;
  private CodingStandardsRules codingStandards;
  private CoverageRules coverage;

}
