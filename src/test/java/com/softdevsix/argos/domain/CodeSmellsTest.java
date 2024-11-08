package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CodeSmellsTest {
    @Test
    void testNoDuplicatedCodeSetterAndGetter() {
        CodeSmells codeSmells = new CodeSmells();
        assertFalse(codeSmells.isNoDuplicatedCodeEnabled());

        codeSmells.setNoDuplicatedCode(true);
        assertTrue(codeSmells.isNoDuplicatedCodeEnabled());
    }

    @Test
    void testMethodTooLongSetterAndGetter() {
        CodeSmells codeSmells = new CodeSmells();
        assertFalse(codeSmells.isMethodTooLongEnabled());

        codeSmells.setMethodTooLong(true);
        assertTrue(codeSmells.isMethodTooLongEnabled());
    }

    @Test
    void testMaxMethodLengthSetterAndGetter() {
        CodeSmells codeSmells = new CodeSmells();
        assertEquals(0, codeSmells.getMaxMethodLength());

        codeSmells.setMaxMethodLength(50);
        assertEquals(50, codeSmells.getMaxMethodLength());
    }

    @Test
    void testExcessiveParametersSetterAndGetter() {
        CodeSmells codeSmells = new CodeSmells();
        assertFalse(codeSmells.isExcessiveParametersEnabled());

        codeSmells.setExcessiveParameters(true);
        assertTrue(codeSmells.isExcessiveParametersEnabled());
    }

    @Test
    void testMaxParametersSetterAndGetter() {
        CodeSmells codeSmells = new CodeSmells();
        assertEquals(0, codeSmells.getMaxParameters());

        codeSmells.setMaxParameters(5);
        assertEquals(5, codeSmells.getMaxParameters());
    }

    @Test
    void testMagicNumbersSetterAndGetter() {
        CodeSmells codeSmells = new CodeSmells();
        assertFalse(codeSmells.isMagicNumbersEnabled());

        codeSmells.setMagicNumbers(true);
        assertTrue(codeSmells.isMagicNumbersEnabled());
    }
}
