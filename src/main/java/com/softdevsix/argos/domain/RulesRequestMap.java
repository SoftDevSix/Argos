package com.softdevsix.argos.domain;

public class RulesRequestMap {
  private CodeQuality codeQuality;
  private BestPractices bestPractices;
  private CodeSmells codeSmells;
  private CodeComplexity codeComplexity;
  private CodingStandards codingStandards;
  private Coverage coverage;

  public CodeQuality getCodeQuality() {
    return codeQuality;
  }

  public BestPractices getBestPractices() {
    return bestPractices;
  }

  public CodeSmells getCodeSmells() {
    return codeSmells;
  }

  public CodeComplexity getCodeComplexity() {
    return codeComplexity;
  }

  public CodingStandards getCodingStandards() {
    return codingStandards;
  }

  public Coverage getCoverage() {
    return coverage;
  }
}
