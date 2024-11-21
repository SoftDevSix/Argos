package com.softdevsix.argos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BestPractices_rules {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = true)
  private boolean noHardcodedValues;

  public boolean isNoHardcodedValuesEnabled() {
    return noHardcodedValues;
  }

  public void setNoHardcodedValues(boolean noHardcodedValues) {
    this.noHardcodedValues = noHardcodedValues;
  }
}
