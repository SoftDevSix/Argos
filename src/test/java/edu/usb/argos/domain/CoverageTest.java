package edu.usb.argos.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {
    @Test
    void testCoverageReviewRequiredSetterAndGetter() {
        CoverageRules coverage = new CoverageRules();
        assertFalse(coverage.isCoverageReviewRequired());

        coverage.setCoverageReviewRequired(true);
        assertTrue(coverage.isCoverageReviewRequired());
    }

    @Test
    void testMinCoveragePercentageSetterAndGetter() {
        CoverageRules coverage = new CoverageRules();
        assertFalse(coverage.isMinCoveragePercentageEnabled());

        coverage.setMinCoveragePercentage(true);
        assertTrue(coverage.isMinCoveragePercentageEnabled());
    }

    @Test
    void testCoverageThresholdSetterAndGetter() {
        CoverageRules coverage = new CoverageRules();
        assertEquals(0, coverage.getCoverageThreshold());

        coverage.setCoverageThreshold(80);
        assertEquals(80, coverage.getCoverageThreshold());
    }

    @Test
    void testRejectIfLowerSetterAndGetter() {
        CoverageRules coverage = new CoverageRules();
        assertFalse(coverage.isRejectIfLowerEnabled());

        coverage.setRejectIfLower(true);
        assertTrue(coverage.isRejectIfLowerEnabled());
    }
}
