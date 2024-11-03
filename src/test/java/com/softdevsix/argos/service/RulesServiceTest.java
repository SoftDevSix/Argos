package com.softdevsix.argos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.softdevsix.argos.domain.*;
import com.softdevsix.argos.service.RulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RulesServiceTests {

    private RulesService rulesService;
    private RulesRequestMap rulesRequestMap;

    @BeforeEach
    void setUp() {
        rulesService = new RulesService();
        rulesRequestMap = new RulesRequestMap();
    }

    @Test
    void testHandleRules_withValidCodeQuality() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLength(true);
        codeQuality.setMaxLineLengthLimit(120);
        rulesRequestMap.setCodeQuality(codeQuality);

        rulesService.handleRules(rulesRequestMap);
        Rules validatedRules = rulesService.getRules();

        assertNotNull(validatedRules.getCodeQuality(), "CodeQuality should not be null");
        assertEquals(120, validatedRules.getCodeQuality().getMaxLineLengthLimit(), "Max line length should be 120");
    }

    @Test
    void testHandleRules_withInvalidMaxLineLengthLimit() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLength(true);
        codeQuality.setMaxLineLengthLimit(-1); // Valor inválido

        rulesRequestMap.setCodeQuality(codeQuality);

        assertThrows(IllegalArgumentException.class, () -> {
            rulesService.handleRules(rulesRequestMap);
        }, "Should throw exception for negative max line length limit");
    }

    @Test
    void testHandleRules_withValidCodeComplexity() {
        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setCyclomaticComplexityLimit(true);
        codeComplexity.setMaxCyclomaticComplexity(15);
        codeComplexity.setNestingDepthLimit(true);
        codeComplexity.setMaxNestingDepth(5);

        rulesRequestMap.setCodeComplexity(codeComplexity);

        rulesService.handleRules(rulesRequestMap);
        Rules validatedRules = rulesService.getRules();

        assertNotNull(validatedRules.getCodeComplexity(), "CodeComplexity should not be null");
        assertEquals(15, validatedRules.getCodeComplexity().getMaxCyclomaticComplexity(), "Max cyclomatic complexity should be 15");
        assertEquals(5, validatedRules.getCodeComplexity().getMaxNestingDepth(), "Max nesting depth should be 5");
    }

    @Test
    void testHandleRules_withInvalidCyclomaticComplexityLimit() {
        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setCyclomaticComplexityLimit(true);
        codeComplexity.setMaxCyclomaticComplexity(-5); // Valor inválido

        rulesRequestMap.setCodeComplexity(codeComplexity);

        assertThrows(IllegalArgumentException.class, () -> {
            rulesService.handleRules(rulesRequestMap);
        }, "Should throw exception for negative cyclomatic complexity limit");
    }

    @Test
    void testGetRules_afterHandlingRules() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLength(true);
        codeQuality.setMaxLineLengthLimit(100);
        rulesRequestMap.setCodeQuality(codeQuality);

        rulesService.handleRules(rulesRequestMap);
        Rules validatedRules = rulesService.getRules();

        assertNotNull(validatedRules, "Rules should not be null after handling");
        assertEquals(100, validatedRules.getCodeQuality().getMaxLineLengthLimit(), "Max line length limit should be set to 100");
    }

    @Test
    void testHandleRules_withValidBestPractices() {
        BestPractices bestPractices = new BestPractices();
        bestPractices.setNoHardcodedValues(false);
        rulesRequestMap.setBestPractices(bestPractices);

        rulesService.handleRules(rulesRequestMap);
        Rules validatedRules = rulesService.getRules();

        assertNotNull(validatedRules.getBestPractices(), "BestPractices should not be null");
        assertEquals(false, validatedRules.getBestPractices().isNoHardcodedValuesEnabled(), "NoHardcodedValues should be false");
    }
}