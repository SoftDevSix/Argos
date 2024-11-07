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
    Rules rules = new Rules();
    BestPractices bestPractices = new BestPractices();
    CodeComplexity codeComplexity = new CodeComplexity();
    CodeQuality codeQuality = new CodeQuality();
    CodeSmells codeSmells = new CodeSmells();
    CodingStandards codingStandards = new CodingStandards();
    Coverage coverage = new Coverage();

    rules.setBestPractices(bestPractices);
    rules.setCodeComplexity(codeComplexity);
    rules.setCodeQuality(codeQuality);
    rules.setCodeSmells(codeSmells);
    rules.setCodingStandards(codingStandards);
    rules.setCoverage(coverage);

    Project project = new Project();
    projectRepository.save(project);

    Integer projectRuleId = rulesRepo.createRule(rules, project);
    Rules fetchedRule = rulesRepo.fetchRule(projectRuleId);
    assertNotNull(fetchedRule, "The fetched rule should not be null");
  }
}
