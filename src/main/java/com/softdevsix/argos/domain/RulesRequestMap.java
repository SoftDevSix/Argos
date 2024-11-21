package com.softdevsix.argos.domain;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class RulesRequestMap {
  private CodeQuality codeQuality;
  private BestPractices bestPractices;
  private CodeSmells codeSmells;
  private CodeComplexity codeComplexity;
  private CodingStandards codingStandards;
  private Coverage coverage;

}
