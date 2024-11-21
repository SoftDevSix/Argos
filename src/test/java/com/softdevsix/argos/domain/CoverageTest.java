package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {
    @Test
    void testCoverageReviewRequiredSetterAndGetter() {
        Coverage_rules coverage = new Coverage_rules();
        assertFalse(coverage.isCoverageReviewRequired());

        coverage.setCoverageReviewRequired(true);
        assertTrue(coverage.isCoverageReviewRequired());
    }

    @Test
    void testMinCoveragePercentageSetterAndGetter() {
        Coverage_rules coverage = new Coverage_rules();
        assertFalse(coverage.isMinCoveragePercentageEnabled());

        coverage.setMinCoveragePercentage(true);
        assertTrue(coverage.isMinCoveragePercentageEnabled());
    }

    @Test
    void testCoverageThresholdSetterAndGetter() {
        Coverage_rules coverage = new Coverage_rules();
        assertEquals(0, coverage.getCoverageThreshold());

        coverage.setCoverageThreshold(80);
        assertEquals(80, coverage.getCoverageThreshold());
    }

    @Test
    void testRejectIfLowerSetterAndGetter() {
        Coverage_rules coverage = new Coverage_rules();
        assertFalse(coverage.isRejectIfLowerEnabled());

        coverage.setRejectIfLower(true);
        assertTrue(coverage.isRejectIfLowerEnabled());
    }
}
