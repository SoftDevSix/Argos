package com.softdevsix.argos.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class RulesRequestMap {
  private CodeQuality codeQuality;
  private BestPractices bestPractices;
  private CodeSmells codeSmells;
  private CodeComplexity codeComplexity;
  private CodingStandards codingStandards;
  private Coverage coverage;

}
