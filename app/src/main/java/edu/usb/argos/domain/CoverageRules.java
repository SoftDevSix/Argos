package edu.usb.argos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CoverageRules {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;


  @Column(nullable = true)
  private boolean coverageReviewRequired;

  @Column(nullable = true)
  private boolean minCoveragePercentage;

  @Column(nullable = true)
  private boolean rejectIfLower;

  @Column(nullable = true)
  private int coverageThreshold;

  public boolean isMinCoveragePercentageEnabled() {
    return minCoveragePercentage;
  }

  public void setMinCoveragePercentage(boolean minCoveragePercentage) {
    this.minCoveragePercentage = minCoveragePercentage;
  }

  public int getCoverageThreshold() {
    return coverageThreshold;
  }

  public boolean isRejectIfLowerEnabled() {
    return rejectIfLower;
  }

  public void setRejectIfLower(boolean rejectIfLower) {
    this.rejectIfLower = rejectIfLower;
  }

  public boolean isCoverageReviewRequired() {
    return coverageReviewRequired;
  }

  public void setCoverageReviewRequired(boolean coverageReviewRequired) {
    this.coverageReviewRequired = coverageReviewRequired;
  }

public void setCoverageThreshold(int coverage) {
    this.coverageThreshold = coverage;
}
}
