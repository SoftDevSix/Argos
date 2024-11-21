package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CodingStandardsTest {
    @Test
    void testCamelCaseNamingSetterAndGetter() {
        CodingStandards_rules codingStandards = new CodingStandards_rules();
        assertFalse(codingStandards.isCamelCaseNamingEnabled());

        codingStandards.setCamelCaseNaming(true);
        assertTrue(codingStandards.isCamelCaseNamingEnabled());
    }

    @Test
    void testPascalCaseForClassesSetterAndGetter() {
        CodingStandards_rules codingStandards = new CodingStandards_rules();
        assertFalse(codingStandards.isPascalCaseForClassesEnabled());

        codingStandards.setPascalCaseForClasses(true);
        assertTrue(codingStandards.isPascalCaseForClassesEnabled());
    }

    @Test
    void testBracesOnSameLineSetterAndGetter() {
        CodingStandards_rules codingStandards = new CodingStandards_rules();
        assertFalse(codingStandards.isBracesOnSameLineEnabled());

        codingStandards.setBracesOnSameLine(true);
        assertTrue(codingStandards.isBracesOnSameLineEnabled());
    }
}
