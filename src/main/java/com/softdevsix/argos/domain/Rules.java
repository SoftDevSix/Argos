package com.softdevsix.argos.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Rules {
  private BestPractices bestPractices;
  private CodeComplexity codeComplexity;
  private CodeQuality codeQuality;
  private CodeSmells codeSmells;
  private CodingStandards codingStandards;
  private Coverage coverage;
}
