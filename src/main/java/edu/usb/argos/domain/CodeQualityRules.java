package edu.usb.argos.argos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CodeQualityRules {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = true)
  private boolean maxLineLength;

  @Column(nullable = true)
  private boolean noUnusedImports;

  @Column(nullable = true)
  private int maxLineLengthLimit;

  public boolean isMaxLineLengthEnabled() {
    return maxLineLength;
  }

  public void setMaxLineLength(boolean maxLineLength) {
    this.maxLineLength = maxLineLength;
  }

  public int getMaxLineLengthLimit() {
    return maxLineLengthLimit;
  }

  public void setMaxLineLengthLimit(int maxLineLengthLimit) {
    this.maxLineLengthLimit = maxLineLengthLimit;
  }

  public boolean isNoUnusedImportsEnabled() {
    return noUnusedImports;
  }

  public void setNoUnusedImports(boolean noUnusedImports) {
    this.noUnusedImports = noUnusedImports;
  }
}
