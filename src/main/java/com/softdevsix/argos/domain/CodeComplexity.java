package com.softdevsix.argos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CodeComplexity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = true)
  private boolean cyclomaticComplexityLimit;

  @Column(nullable = true)
  private boolean maxMethodCountInClass;

  @Column(nullable = true)
  private boolean nestingDepthLimit;

  @Column(nullable = true)
  private int maxCyclomaticComplexity;

  @Column(nullable = true)
  private int maxMethodsInClass;

  @Column(nullable = true)
  private int maxNestingDepth;

  public boolean isCyclomaticComplexityLimitEnabled() {
    return cyclomaticComplexityLimit;
  }

  public void setCyclomaticComplexityLimit(boolean cyclomaticComplexityLimit) {
    this.cyclomaticComplexityLimit = cyclomaticComplexityLimit;
  }

  public int getMaxCyclomaticComplexity() {
    return maxCyclomaticComplexity;
  }

  public void setMaxCyclomaticComplexity(int maxCyclomaticComplexity) {
    this.maxCyclomaticComplexity = maxCyclomaticComplexity;
  }

  public boolean isNestingDepthLimitEnabled() {
    return nestingDepthLimit;
  }

  public void setNestingDepthLimit(boolean nestingDepthLimit) {
    this.nestingDepthLimit = nestingDepthLimit;
  }

  public int getMaxNestingDepth() {
    return maxNestingDepth;
  }

  public void setMaxNestingDepth(int maxNestingDepth) {
    this.maxNestingDepth = maxNestingDepth;
  }

  public boolean isMaxMethodCountInClassEnabled() {
    return maxMethodCountInClass;
  }

  public void setMaxMethodCountInClass(boolean maxMethodCountInClass) {
    this.maxMethodCountInClass = maxMethodCountInClass;
  }

  public int getMaxMethodsInClass() {
    return maxMethodsInClass;
  }

  public void setMaxMethodsInClass(int maxMethodsInClass) {
    this.maxMethodsInClass = maxMethodsInClass;
  }
}
