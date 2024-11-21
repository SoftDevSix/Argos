package com.softdevsix.argos.domain;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class RulesRequestMap {
  private CodeQuality_rules codeQuality;
  private BestPractices_rules bestPractices;
  private CodeSmells_rules codeSmells;
  private CodeComplexity_rules codeComplexity;
  private CodingStandards_rules codingStandards;
  private Coverage_rules coverage;

}
