package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeComplexityTest {
    @Test
    void testCyclomaticComplexityLimitSetterAndGetter() {
        CodeComplexity codeComplexity = new CodeComplexity();

        assertFalse(codeComplexity.isCyclomaticComplexityLimitEnabled());

        codeComplexity.setCyclomaticComplexityLimit(true);
        assertTrue(codeComplexity.isCyclomaticComplexityLimitEnabled());
    }

    @Test
    void testMaxCyclomaticComplexitySetterAndGetter() {
        CodeComplexity codeComplexity = new CodeComplexity();

        assertEquals(0, codeComplexity.getMaxCyclomaticComplexity());

        codeComplexity.setMaxCyclomaticComplexity(10);
        assertEquals(10, codeComplexity.getMaxCyclomaticComplexity());
    }

    @Test
    void testNestingDepthLimitSetterAndGetter() {
        CodeComplexity codeComplexity = new CodeComplexity();

        assertFalse(codeComplexity.isNestingDepthLimitEnabled());

        codeComplexity.setNestingDepthLimit(true);
        assertTrue(codeComplexity.isNestingDepthLimitEnabled());
    }

    @Test
    void testMaxNestingDepthSetterAndGetter() {
        CodeComplexity codeComplexity = new CodeComplexity();

        assertEquals(0, codeComplexity.getMaxNestingDepth());

        codeComplexity.setMaxNestingDepth(5);
        assertEquals(5, codeComplexity.getMaxNestingDepth());
    }

    @Test
    void testMaxMethodCountInClassSetterAndGetter() {
        CodeComplexity codeComplexity = new CodeComplexity();

        assertFalse(codeComplexity.isMaxMethodCountInClassEnabled());

        codeComplexity.setMaxMethodCountInClass(true);
        assertTrue(codeComplexity.isMaxMethodCountInClassEnabled());
    }

    @Test
    void testMaxMethodsInClassSetterAndGetter() {
        CodeComplexity codeComplexity = new CodeComplexity();

        assertEquals(0, codeComplexity.getMaxMethodsInClass());

        codeComplexity.setMaxMethodsInClass(20);
        assertEquals(20, codeComplexity.getMaxMethodsInClass());
    }
}
