package edu.usb.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CodingStandardsTest {
    @Test
    void testCamelCaseNamingSetterAndGetter() {
        CodingStandardsRules codingStandards = new CodingStandardsRules();
        assertFalse(codingStandards.isCamelCaseNamingEnabled());

        codingStandards.setCamelCaseNaming(true);
        assertTrue(codingStandards.isCamelCaseNamingEnabled());
    }

    @Test
    void testPascalCaseForClassesSetterAndGetter() {
        CodingStandardsRules codingStandards = new CodingStandardsRules();
        assertFalse(codingStandards.isPascalCaseForClassesEnabled());

        codingStandards.setPascalCaseForClasses(true);
        assertTrue(codingStandards.isPascalCaseForClassesEnabled());
    }

    @Test
    void testBracesOnSameLineSetterAndGetter() {
        CodingStandardsRules codingStandards = new CodingStandardsRules();
        assertFalse(codingStandards.isBracesOnSameLineEnabled());

        codingStandards.setBracesOnSameLine(true);
        assertTrue(codingStandards.isBracesOnSameLineEnabled());
    }
}
