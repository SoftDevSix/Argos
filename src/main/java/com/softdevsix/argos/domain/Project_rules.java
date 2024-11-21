package com.softdevsix.argos.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project_rules {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bestPractices_id")
  private BestPractices_rules bestPractices;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeComplexity_Id")
  private CodeComplexity_rules codeComplexity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeQuality_id")
  private CodeQuality_rules codeQuality;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeSmells_id")
  private CodeSmells_rules codeSmells;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codingStandards_id")
  private CodingStandards_rules codingStandards;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coverage_id")
  private Coverage_rules coverage;
}
