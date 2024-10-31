package com.softdevsix.argos.domain;

public class CodeComplexity {

    private boolean cyclomaticComplexityLimit = true;
    private int maxCyclomaticComplexity = 10; 
    private boolean nestingDepthLimit = true;
    private int maxNestingDepth = 3; 
    private boolean maxMethodCountInClass = true;
    private int maxMethodsInClass = 20;
    
    public CodeComplexity() {
    }

    public boolean isCyclomaticComplexityLimitEnabled() { return cyclomaticComplexityLimit; }
    public void setCyclomaticComplexityLimit(boolean cyclomaticComplexityLimit) { this.cyclomaticComplexityLimit = cyclomaticComplexityLimit; }

    public int getMaxCyclomaticComplexity() { return maxCyclomaticComplexity; }
    public void setMaxCyclomaticComplexity(int maxCyclomaticComplexity) { this.maxCyclomaticComplexity = maxCyclomaticComplexity; }

    public boolean isNestingDepthLimitEnabled() { return nestingDepthLimit; }
    public void setNestingDepthLimit(boolean nestingDepthLimit) { this.nestingDepthLimit = nestingDepthLimit; }

    public int getMaxNestingDepth() { return maxNestingDepth; }
    public void setMaxNestingDepth(int maxNestingDepth) { this.maxNestingDepth = maxNestingDepth; }

    public boolean isMaxMethodCountInClassEnabled() { return maxMethodCountInClass; }
    public void setMaxMethodCountInClass(boolean maxMethodCountInClass) { this.maxMethodCountInClass = maxMethodCountInClass; }

    public int getMaxMethodsInClass() { return maxMethodsInClass; }
    public void setMaxMethodsInClass(int maxMethodsInClass) { this.maxMethodsInClass = maxMethodsInClass; }
}
