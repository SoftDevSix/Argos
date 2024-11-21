package edu.usb.argos.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CodeSmellsTest {
    @Test
    void testNoDuplicatedCodeSetterAndGetter() {
        CodeSmellsRules codeSmells = new CodeSmellsRules();
        assertFalse(codeSmells.isNoDuplicatedCodeEnabled());

        codeSmells.setNoDuplicatedCode(true);
        assertTrue(codeSmells.isNoDuplicatedCodeEnabled());
    }

    @Test
    void testMethodTooLongSetterAndGetter() {
        CodeSmellsRules codeSmells = new CodeSmellsRules();
        assertFalse(codeSmells.isMethodTooLongEnabled());

        codeSmells.setMethodTooLong(true);
        assertTrue(codeSmells.isMethodTooLongEnabled());
    }

    @Test
    void testMaxMethodLengthSetterAndGetter() {
        CodeSmellsRules codeSmells = new CodeSmellsRules();
        assertEquals(0, codeSmells.getMaxMethodLength());

        codeSmells.setMaxMethodLength(50);
        assertEquals(50, codeSmells.getMaxMethodLength());
    }

    @Test
    void testExcessiveParametersSetterAndGetter() {
        CodeSmellsRules codeSmells = new CodeSmellsRules();
        assertFalse(codeSmells.isExcessiveParametersEnabled());

        codeSmells.setExcessiveParameters(true);
        assertTrue(codeSmells.isExcessiveParametersEnabled());
    }

    @Test
    void testMaxParametersSetterAndGetter() {
        CodeSmellsRules codeSmells = new CodeSmellsRules();
        assertEquals(0, codeSmells.getMaxParameters());

        codeSmells.setMaxParameters(5);
        assertEquals(5, codeSmells.getMaxParameters());
    }

    @Test
    void testMagicNumbersSetterAndGetter() {
        CodeSmellsRules codeSmells = new CodeSmellsRules();
        assertFalse(codeSmells.isMagicNumbersEnabled());

        codeSmells.setMagicNumbers(true);
        assertTrue(codeSmells.isMagicNumbersEnabled());
    }
}
