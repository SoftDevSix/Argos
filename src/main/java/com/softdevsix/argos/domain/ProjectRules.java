package com.softdevsix.argos.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProjectRules {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "repository_id")
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bestPractices_id")
  private BestPractices bestPractices;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeComplexity_Id")
  private CodeComplexity codeComplexity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeQuality_id")
  private CodeQuality codeQuality;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeSmells_id")
  private CodeSmells codeSmells;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codingStandards_id")
  private CodingStandards codingStandards;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coverage_id")
  private Coverage coverage;

  public ProjectRules() {}

  public ProjectRules(
      Project project,
      BestPractices bestPractices,
      CodeComplexity codeComplexity,
      CodeQuality codeQuality,
      CodeSmells codeSmells,
      CodingStandards codingStandards,
      Coverage coverage) {
    this.project = project;
    this.bestPractices = bestPractices;
    this.codeComplexity = codeComplexity;
    this.codeQuality = codeQuality;
    this.codeSmells = codeSmells;
    this.codingStandards = codingStandards;
    this.coverage = coverage;
  }

  public Project getProject() {
    return project;
  }

  public BestPractices getBestPractices() {
    return bestPractices;
  }

  public CodeComplexity getCodeComplexity() {
    return codeComplexity;
  }

  public CodeQuality getCodeQuality() {
    return codeQuality;
  }

  public CodeSmells getCodeSmells() {
    return codeSmells;
  }

  public CodingStandards getCodingStandards() {
    return codingStandards;
  }

  public Coverage getCoverage() {
    return coverage;
  }

  public Integer getId() {
    return id;
  }
}
