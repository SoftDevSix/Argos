package com.softdevsix.argos.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rules {
  private BestPractices bestPractices;
  private CodeComplexity codeComplexity;
  private CodeQuality codeQuality;
  private CodeSmells codeSmells;
  private CodingStandards codingStandards;
  private Coverage coverage;

}
