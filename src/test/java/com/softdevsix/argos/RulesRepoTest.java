package com.softdevsix.argos;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.softdevsix.argos.domain.BestPractices;
import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.CodeSmells;
import com.softdevsix.argos.domain.CodingStandards;
import com.softdevsix.argos.domain.Coverage;
import com.softdevsix.argos.domain.Rules;
import com.softdevsix.argos.repository.RulesRepoImpl;

/**
 * RulesRepoTest
 */
@SpringBootTest
class RulesRepoTest {

  @Autowired RulesRepoImpl rulesRepo;
  @Test
  void verifyRepoWorks() throws Exception {
    Rules rules = new Rules();
    BestPractices bestPractices = new BestPractices();
    CodeComplexity codeComplexity = new CodeComplexity();
    CodeQuality codeQuality = new CodeQuality();
    CodeSmells codeSmells = new CodeSmells();
    CodingStandards codingStandards = new CodingStandards();
    Coverage coverage = new Coverage();

    int repositoryId = 1;
    bestPractices.setRepositoryId(repositoryId);
    codeComplexity.setRepositoryId(repositoryId);
    codeQuality.setRepositoryId(repositoryId);
    codeSmells.setRepositoryId(repositoryId);
    codingStandards.setRepositoryId(repositoryId);
    coverage.setRepositoryId(repositoryId);

    rules.setBestPractices(bestPractices);
    rules.setCodeComplexity(codeComplexity);
    rules.setCodeQuality(codeQuality);
    rules.setCodeSmells(codeSmells);
    rules.setCodingStandards(codingStandards);
    rules.setCoverage(coverage);

    try {
      rulesRepo.createRule(rules);

      Rules fetchedRule = rulesRepo.fetchRule(repositoryId);  
      assertNotNull(fetchedRule, "The fetched rule should not be null");
    } catch (Exception e) {
      throw new Exception(e);
    }
  }
}
