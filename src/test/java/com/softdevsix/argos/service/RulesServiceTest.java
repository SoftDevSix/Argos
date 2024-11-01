package com.softdevsix.argos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.softdevsix.argos.domain.RulesRequestMap;
import com.softdevsix.argos.domain.BestPractices;
import com.softdevsix.argos.domain.CodeSmells;
import com.softdevsix.argos.domain.CodeComplexity;
import com.softdevsix.argos.domain.CodeQuality;
import com.softdevsix.argos.domain.Coverage;
import com.softdevsix.argos.service.RulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RulesServiceTest {

    @Autowired
    private RulesService rulesService;

    private RulesRequestMap rulesRequestMap;

    @BeforeEach
    void setUp() {
        rulesRequestMap = new RulesRequestMap();
    }

    @Test
    void testHandleRules_ValidRules() {
        setUpValidRules();
        
        rulesService.handleRules(rulesRequestMap);
        
        assertTrue(rulesService.getValidationErrors().isEmpty(), "No validation errors expected.");
        assertNotNull(rulesService.getRules(), "Rules should be saved when valid.");
    }

    @Test
    void testHandleRules_NoHardcodedValuesError() {
        BestPractices bestPractices = new BestPractices();
        bestPractices.setNoHardcodedValues(false);
        rulesRequestMap.setBestPractices(bestPractices);

        rulesService.handleRules(rulesRequestMap);

        assertEquals(1, rulesService.getValidationErrors().size());
        assertEquals("Best Practices - Avoid hardcoded values.", rulesService.getValidationErrors().get(0));
    }

    @Test
    void testHandleRules_LongMethodEnabledError() {
        CodeSmells codeSmells = new CodeSmells();
        codeSmells.setMethodTooLong(true);
        codeSmells.setMaxMethodLength(0);
        rulesRequestMap.setCodeSmells(codeSmells);

        rulesService.handleRules(rulesRequestMap);

        assertEquals(1, rulesService.getValidationErrors().size());
        assertEquals("Code Smells - Method too long is enabled, but max length must be positive.", 
                      rulesService.getValidationErrors().get(0));
    }

    @Test
    void testHandleRules_CyclomaticComplexityLimitError() {
        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setMaxCyclomaticComplexity(0);
        rulesRequestMap.setCodeComplexity(codeComplexity);

        rulesService.handleRules(rulesRequestMap);

        assertEquals(1, rulesService.getValidationErrors().size());
        assertEquals("Code Complexity - Cyclomatic complexity limit must be positive.", 
                      rulesService.getValidationErrors().get(0));
    }

    @Test
    void testHandleRules_MaxLineLengthError() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLengthLimit(0);
        rulesRequestMap.setCodeQuality(codeQuality);

        rulesService.handleRules(rulesRequestMap);

        assertEquals(1, rulesService.getValidationErrors().size());
        assertEquals("Code Quality - Max line length must be positive.", 
                      rulesService.getValidationErrors().get(0));
    }

    @Test
    void testHandleRules_MinCoveragePercentageError() {
        Coverage coverage = new Coverage();
        coverage.setCoverageThreshold(150);
        rulesRequestMap.setCoverage(coverage);

        rulesService.handleRules(rulesRequestMap);

        assertEquals(1, rulesService.getValidationErrors().size());
        assertEquals("Coverage - Min coverage percentage must be between 0 and 100.", 
                      rulesService.getValidationErrors().get(0));
    }

    @Test
    void testGetValidationErrors() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLengthLimit(0);
        rulesRequestMap.setCodeQuality(codeQuality);
        
        rulesService.handleRules(rulesRequestMap);
        
        assertEquals(1, rulesService.getValidationErrors().size(), "Expected one validation error.");
    }

    @Test
    void testGetRules() {
        setUpValidRules();

        rulesService.handleRules(rulesRequestMap);

        assertNotNull(rulesService.getRules(), "Rules should be saved when valid.");
    }

    private void setUpValidRules() {
        BestPractices bestPractices = new BestPractices();
        bestPractices.setNoHardcodedValues(true);
        rulesRequestMap.setBestPractices(bestPractices);

        CodeSmells codeSmells = new CodeSmells();
        codeSmells.setMethodTooLong(true);
        codeSmells.setMaxMethodLength(10);
        rulesRequestMap.setCodeSmells(codeSmells);

        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setMaxCyclomaticComplexity(10);
        rulesRequestMap.setCodeComplexity(codeComplexity);

        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLengthLimit(80);
        rulesRequestMap.setCodeQuality(codeQuality);

        Coverage coverage = new Coverage();
        coverage.setCoverageThreshold(85);
        rulesRequestMap.setCoverage(coverage);
    }
}
