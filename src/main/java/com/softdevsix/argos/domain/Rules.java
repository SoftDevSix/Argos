package com.softdevsix.argos.domain;

import lombok.Builder;
import lombok.Generated;
import lombok.Value;

@Generated
@Value
@Builder
public class Rules {
  private BestPractices_rules bestPractices;
  private CodeComplexity_rules codeComplexity;
  private CodeQuality_rules codeQuality;
  private CodeSmells_rules codeSmells;
  private CodingStandards_rules codingStandards;
  private Coverage_rules coverage;
}
