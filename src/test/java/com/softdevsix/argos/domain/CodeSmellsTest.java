package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CodeSmellsTest {
    @Test
    void testNoDuplicatedCodeSetterAndGetter() {
        CodeSmells_rules codeSmells = new CodeSmells_rules();
        assertFalse(codeSmells.isNoDuplicatedCodeEnabled());

        codeSmells.setNoDuplicatedCode(true);
        assertTrue(codeSmells.isNoDuplicatedCodeEnabled());
    }

    @Test
    void testMethodTooLongSetterAndGetter() {
        CodeSmells_rules codeSmells = new CodeSmells_rules();
        assertFalse(codeSmells.isMethodTooLongEnabled());

        codeSmells.setMethodTooLong(true);
        assertTrue(codeSmells.isMethodTooLongEnabled());
    }

    @Test
    void testMaxMethodLengthSetterAndGetter() {
        CodeSmells_rules codeSmells = new CodeSmells_rules();
        assertEquals(0, codeSmells.getMaxMethodLength());

        codeSmells.setMaxMethodLength(50);
        assertEquals(50, codeSmells.getMaxMethodLength());
    }

    @Test
    void testExcessiveParametersSetterAndGetter() {
        CodeSmells_rules codeSmells = new CodeSmells_rules();
        assertFalse(codeSmells.isExcessiveParametersEnabled());

        codeSmells.setExcessiveParameters(true);
        assertTrue(codeSmells.isExcessiveParametersEnabled());
    }

    @Test
    void testMaxParametersSetterAndGetter() {
        CodeSmells_rules codeSmells = new CodeSmells_rules();
        assertEquals(0, codeSmells.getMaxParameters());

        codeSmells.setMaxParameters(5);
        assertEquals(5, codeSmells.getMaxParameters());
    }

    @Test
    void testMagicNumbersSetterAndGetter() {
        CodeSmells_rules codeSmells = new CodeSmells_rules();
        assertFalse(codeSmells.isMagicNumbersEnabled());

        codeSmells.setMagicNumbers(true);
        assertTrue(codeSmells.isMagicNumbersEnabled());
    }
}
