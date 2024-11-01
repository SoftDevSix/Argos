package com.softdevsix.argos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CodingStandards {

  @Id @GeneratedValue(strategy = GenerationType.AUTO) private Integer id;
  private Integer repositoryId;
  @Column(nullable = true) private boolean camelCaseNaming;
  @Column(nullable = true) private boolean pascalCaseForClasses;
  @Column(nullable = true) private boolean bracesOnSameLine;

  public boolean isCamelCaseNamingEnabled() { return camelCaseNaming; }
  public void setCamelCaseNaming(boolean camelCaseNaming) {
    this.camelCaseNaming = camelCaseNaming;
  }

  public boolean isPascalCaseForClassesEnabled() {
    return pascalCaseForClasses;
  }
  public void setPascalCaseForClasses(boolean pascalCaseForClasses) {
    this.pascalCaseForClasses = pascalCaseForClasses;
  }

  public boolean isBracesOnSameLineEnabled() { return bracesOnSameLine; }
  public void setBracesOnSameLine(boolean bracesOnSameLine) {
    this.bracesOnSameLine = bracesOnSameLine;
  }
  public Integer getRepositoryId() { return repositoryId; }
  public void setRepositoryId(Integer repositoryId) {
    this.repositoryId = repositoryId;
  }
}
