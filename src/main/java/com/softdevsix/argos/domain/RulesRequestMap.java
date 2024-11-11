package com.softdevsix.argos.domain;

public class RulesRequestMap {
    private CodeQuality codeQuality;
    private BestPractices bestPractices;
    private CodeSmells codeSmells;
    private CodeComplexity codeComplexity;
    private CodingStandards codingStandards;
    private Coverage coverage;

    public CodeQuality getCodeQuality() { return codeQuality; }
    public void setCodeQuality(CodeQuality codeQuality) { this.codeQuality = codeQuality; }

    public BestPractices getBestPractices() { return bestPractices; }
    public void setBestPractices(BestPractices bestPractices) { this.bestPractices = bestPractices; }

    public CodeSmells getCodeSmells() { return codeSmells; }
    public void setCodeSmells(CodeSmells codeSmells) { this.codeSmells = codeSmells; }

    public CodeComplexity getCodeComplexity() { return codeComplexity; }
    public void setCodeComplexity(CodeComplexity codeComplexity) { this.codeComplexity = codeComplexity; }

    public CodingStandards getCodingStandards() { return codingStandards; }
    public void setCodingStandards(CodingStandards codingStandards) { this.codingStandards = codingStandards; }

    public Coverage getCoverage() { return coverage; }
    public void setCoverage(Coverage coverage) { this.coverage = coverage; }
}
