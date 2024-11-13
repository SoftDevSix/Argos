package com.softdevsix.argos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.softdevsix.argos.domain.*;
import com.softdevsix.argos.exception.RulesValidatorException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RulesValidatorTest {

    private RulesValidator rulesValidator;
    private RulesRequestMap rulesRequestMap;

    @BeforeEach
    void setUp() {
        rulesValidator = new RulesValidator();
        rulesRequestMap = new RulesRequestMap();
    }

    @Test
    void testValidate_withValidCodeQuality() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLength(true);
        codeQuality.setMaxLineLengthLimit(120);
        rulesRequestMap.setCodeQuality(codeQuality);

        Rules validatedRules = rulesValidator.validate(rulesRequestMap);

        assertNotNull(validatedRules.getCodeQuality(), "CodeQuality should not be null");
        assertEquals(120, validatedRules.getCodeQuality().getMaxLineLengthLimit(), "Max line length should be 120");
    }

    @Test
    void testValidate_withInvalidCodeQuality() {
        CodeQuality codeQuality = new CodeQuality();
        codeQuality.setMaxLineLength(true);
        codeQuality.setMaxLineLengthLimit(-1); 
        rulesRequestMap.setCodeQuality(codeQuality);

        assertThrows(RulesValidatorException.class, () -> rulesValidator.validate(rulesRequestMap),
        "The line length limit must be positive");
    }

    @Test
    void testValidate_withValidBestPractices() {
        BestPractices bestPractices = new BestPractices();
        bestPractices.setNoHardcodedValues(false);
        rulesRequestMap.setBestPractices(bestPractices);

        Rules validatedRules = rulesValidator.validate(rulesRequestMap);

        assertNotNull(validatedRules.getBestPractices(), "BestPractices should not be null");
        assertEquals(false, validatedRules.getBestPractices().isNoHardcodedValuesEnabled(), "NoHardcodedValues should be false");
    }

    @Test
    void testValidate_withValidCodeSmells() {
        CodeSmells codeSmells = new CodeSmells();
        codeSmells.setMethodTooLong(true);
        codeSmells.setMaxMethodLength(50);
        rulesRequestMap.setCodeSmells(codeSmells);

        Rules validatedRules = rulesValidator.validate(rulesRequestMap);

        assertNotNull(validatedRules.getCodeSmells(), "CodeSmells should not be null");
        assertEquals(50, validatedRules.getCodeSmells().getMaxMethodLength(), "Max method length should be 50");
    }

    @Test
    void testValidate_withInvalidCodeSmells() {
        CodeSmells codeSmells = new CodeSmells();
        codeSmells.setMethodTooLong(true);
        codeSmells.setMaxMethodLength(-10); 
        rulesRequestMap.setCodeSmells(codeSmells);

        assertThrows(RulesValidatorException.class, () -> rulesValidator.validate(rulesRequestMap),
        "The method length limit must be positive");
    }

    @Test
    void testValidate_withValidCodeComplexity() {
        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setCyclomaticComplexityLimit(true);
        codeComplexity.setMaxCyclomaticComplexity(10);
        codeComplexity.setNestingDepthLimit(true);
        codeComplexity.setMaxNestingDepth(5);
        rulesRequestMap.setCodeComplexity(codeComplexity);

        Rules validatedRules = rulesValidator.validate(rulesRequestMap);

        assertNotNull(validatedRules.getCodeComplexity(), "CodeComplexity should not be null");
        assertEquals(10, validatedRules.getCodeComplexity().getMaxCyclomaticComplexity(), "Cyclomatic complexity should be 10");
        assertEquals(5, validatedRules.getCodeComplexity().getMaxNestingDepth(), "Nesting depth should be 5");
    }

    @Test
    void testValidate_withInvalidCyclomaticComplexity() {
        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setCyclomaticComplexityLimit(true);
        codeComplexity.setMaxCyclomaticComplexity(-5); 
        rulesRequestMap.setCodeComplexity(codeComplexity);

        assertThrows(RulesValidatorException.class, () -> rulesValidator.validate(rulesRequestMap),
        "The cyclomatic complexity limit must be positive");
    }

    @Test
    void testValidate_withInvalidNestingDepth() {
        CodeComplexity codeComplexity = new CodeComplexity();
        codeComplexity.setNestingDepthLimit(true);
        codeComplexity.setMaxNestingDepth(-3); 
        rulesRequestMap.setCodeComplexity(codeComplexity);

        assertThrows(RulesValidatorException.class, () -> rulesValidator.validate(rulesRequestMap),
        "The nesting depth limit must be positive.");
    }

    @Test
    void testValidate_withValidCoverage() {
        Coverage coverage = new Coverage();
        coverage.setMinCoveragePercentage(true);
        coverage.setCoverageThreshold(80);
        rulesRequestMap.setCoverage(coverage);

        Rules validatedRules = rulesValidator.validate(rulesRequestMap);

        assertNotNull(validatedRules.getCoverage(), "Coverage should not be null");
        assertEquals(80, validatedRules.getCoverage().getCoverageThreshold(), "Coverage threshold should be 80");
    }

    @Test
    void testValidate_withInvalidCoverage() {
        Coverage coverage = new Coverage();
        coverage.setMinCoveragePercentage(true);
        coverage.setCoverageThreshold(-10); 
        rulesRequestMap.setCoverage(coverage);

        assertThrows(RulesValidatorException.class, () -> rulesValidator.validate(rulesRequestMap),
        "The coverage threshold must be non-negative");
    }
}
