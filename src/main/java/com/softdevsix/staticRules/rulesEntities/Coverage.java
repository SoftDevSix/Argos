package com.softdevsix.staticRules.rulesEntities;

public class Coverage {

    private boolean minCoveragePercentage = true;
    private int coverageThreshold = 80; 
    private boolean rejectIfLower = true;
    private boolean coverageReviewRequired = true;

    public Coverage() {
    }

    public boolean isMinCoveragePercentageEnabled() { return minCoveragePercentage; }
    public void setMinCoveragePercentage(boolean minCoveragePercentage) { this.minCoveragePercentage = minCoveragePercentage; }

    public int getCoverageThreshold() { return coverageThreshold; }
    public void setCoverageThreshold(int coverageThreshold) { this.coverageThreshold = coverageThreshold; }

    public boolean isRejectIfLowerEnabled() { return rejectIfLower; }
    public void setRejectIfLower(boolean rejectIfLower) { this.rejectIfLower = rejectIfLower; }

    public boolean isCoverageReviewRequired() { return coverageReviewRequired; }
    public void setCoverageReviewRequired(boolean coverageReviewRequired) { this.coverageReviewRequired = coverageReviewRequired; }
}
