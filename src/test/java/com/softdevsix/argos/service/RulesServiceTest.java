package com.softdevsix.argos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.Project;
import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.repository.RulesRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RulesServiceTests {

  @Mock private RulesRepoImpl rulesRepo;

  @Mock private ProjectValidator projectValidator;

  @InjectMocks private RulesService rulesService;

  private RulesRequestMap rulesRequestMap;
  private Project mockProject;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    rulesRequestMap = new RulesRequestMap();
    mockProject = new Project();
    mockProject.setId(1);
  }

  @Test
  void testHandleRules_withInvalidMaxLineLengthLimit() {
    CodeQuality codeQuality = new CodeQuality();
    codeQuality.setMaxLineLength(true);
    codeQuality.setMaxLineLengthLimit(-1);
    rulesRequestMap.setCodeQuality(codeQuality);
    Integer id = mockProject.getId();

    try {
      rulesService.handleRules(rulesRequestMap, id);
      fail("Expected IllegalArgumentException to be thrown");

    } catch (IllegalArgumentException e) {
      assertEquals("The line length limit must be positive", e.getMessage());
    }
  }

  @Test
  void testHandleRules_withInvalidCyclomaticComplexityLimit() {
    CodeComplexity codeComplexity = new CodeComplexity();
    codeComplexity.setCyclomaticComplexityLimit(true);
    codeComplexity.setMaxCyclomaticComplexity(-5);
    rulesRequestMap.setCodeComplexity(codeComplexity);
    Integer id = mockProject.getId();

    try {
      rulesService.handleRules(rulesRequestMap, id);
      fail("Expected IllegalArgumentException to be thrown");

    } catch (IllegalArgumentException e) {
      assertEquals("The cyclomatic complexity limit must be positive", e.getMessage());
    }
  }
}
