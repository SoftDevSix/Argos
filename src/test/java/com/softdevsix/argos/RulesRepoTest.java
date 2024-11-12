package com.softdevsix.argos;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.softdevsix.argos.domain.BestPractices;
import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.CodeSmells;
import com.softdevsix.argos.domain.CodingStandards;
import com.softdevsix.argos.domain.Coverage;
import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.repository.ProjectRepository;
import com.softdevsix.argos.repository.RulesRepoImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** RulesRepoTest */
@SpringBootTest
class RulesRepoTest {

  @Autowired RulesRepoImpl rulesRepo;
  @Autowired ProjectRepository projectRepository;

  @Test
  void verifyRepoWorks() {
    BestPractices bestPractices = new BestPractices();
    CodeComplexity codeComplexity = new CodeComplexity();
    CodeQuality codeQuality = new CodeQuality();
    CodeSmells codeSmells = new CodeSmells();
    CodingStandards codingStandards = new CodingStandards();
    Coverage coverage = new Coverage();

    Rules rules = Rules.builder()
        .bestPractices(bestPractices)
        .codeComplexity(codeComplexity)
        .codeQuality(codeQuality)
        .codeSmells(codeSmells)
        .codingStandards(codingStandards)
        .coverage(coverage)
        .build();

    Project project = new Project();
    projectRepository.save(project);

    Integer projectRuleId = rulesRepo.createRule(rules, project);
    Rules fetchedRule = rulesRepo.fetchRule(projectRuleId).orElseThrow(() -> new RuntimeException("Rule not found"));
    assertNotNull(fetchedRule, "The fetched rule should not be null");
  }
}
