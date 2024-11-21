package edu.usb.argos.argos.domain;

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
public class ProjectRules {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bestPractices_id")
  private BestPracticesRules bestPractices;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeComplexity_Id")
  private CodeComplexityRules codeComplexity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeQuality_id")
  private CodeQualityRules codeQuality;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codeSmells_id")
  private CodeSmellsRules codeSmells;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codingStandards_id")
  private CodingStandardsRules codingStandards;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coverage_id")
  private CoverageRules coverage;
}
