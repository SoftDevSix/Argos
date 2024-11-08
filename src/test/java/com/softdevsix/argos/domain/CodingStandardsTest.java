package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CodingStandardsTest {
    @Test
    void testCamelCaseNamingSetterAndGetter() {
        CodingStandards codingStandards = new CodingStandards();
        assertFalse(codingStandards.isCamelCaseNamingEnabled());

        codingStandards.setCamelCaseNaming(true);
        assertTrue(codingStandards.isCamelCaseNamingEnabled());
    }

    @Test
    void testPascalCaseForClassesSetterAndGetter() {
        CodingStandards codingStandards = new CodingStandards();
        assertFalse(codingStandards.isPascalCaseForClassesEnabled());

        codingStandards.setPascalCaseForClasses(true);
        assertTrue(codingStandards.isPascalCaseForClassesEnabled());
    }

    @Test
    void testBracesOnSameLineSetterAndGetter() {
        CodingStandards codingStandards = new CodingStandards();
        assertFalse(codingStandards.isBracesOnSameLineEnabled());

        codingStandards.setBracesOnSameLine(true);
        assertTrue(codingStandards.isBracesOnSameLineEnabled());
    }
}
