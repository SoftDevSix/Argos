package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeComplexityTest {
    @Test
    void testCyclomaticComplexityLimitSetterAndGetter() {
        CodeComplexityRules codeComplexity = new CodeComplexityRules();

        assertFalse(codeComplexity.isCyclomaticComplexityLimitEnabled());

        codeComplexity.setCyclomaticComplexityLimit(true);
        assertTrue(codeComplexity.isCyclomaticComplexityLimitEnabled());
    }

    @Test
    void testMaxCyclomaticComplexitySetterAndGetter() {
        CodeComplexityRules codeComplexity = new CodeComplexityRules();

        assertEquals(0, codeComplexity.getMaxCyclomaticComplexity());

        codeComplexity.setMaxCyclomaticComplexity(10);
        assertEquals(10, codeComplexity.getMaxCyclomaticComplexity());
    }

    @Test
    void testNestingDepthLimitSetterAndGetter() {
        CodeComplexityRules codeComplexity = new CodeComplexityRules();

        assertFalse(codeComplexity.isNestingDepthLimitEnabled());

        codeComplexity.setNestingDepthLimit(true);
        assertTrue(codeComplexity.isNestingDepthLimitEnabled());
    }

    @Test
    void testMaxNestingDepthSetterAndGetter() {
        CodeComplexityRules codeComplexity = new CodeComplexityRules();

        assertEquals(0, codeComplexity.getMaxNestingDepth());

        codeComplexity.setMaxNestingDepth(5);
        assertEquals(5, codeComplexity.getMaxNestingDepth());
    }

    @Test
    void testMaxMethodCountInClassSetterAndGetter() {
        CodeComplexityRules codeComplexity = new CodeComplexityRules();

        assertFalse(codeComplexity.isMaxMethodCountInClassEnabled());

        codeComplexity.setMaxMethodCountInClass(true);
        assertTrue(codeComplexity.isMaxMethodCountInClassEnabled());
    }

    @Test
    void testMaxMethodsInClassSetterAndGetter() {
        CodeComplexityRules codeComplexity = new CodeComplexityRules();

        assertEquals(0, codeComplexity.getMaxMethodsInClass());

        codeComplexity.setMaxMethodsInClass(20);
        assertEquals(20, codeComplexity.getMaxMethodsInClass());
    }
}
