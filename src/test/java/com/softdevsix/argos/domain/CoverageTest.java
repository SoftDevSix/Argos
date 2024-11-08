package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoverageTest {
    @Test
    void testCoverageReviewRequiredSetterAndGetter() {
        Coverage coverage = new Coverage();
        assertFalse(coverage.isCoverageReviewRequired());

        coverage.setCoverageReviewRequired(true);
        assertTrue(coverage.isCoverageReviewRequired());
    }

    @Test
    void testMinCoveragePercentageSetterAndGetter() {
        Coverage coverage = new Coverage();
        assertFalse(coverage.isMinCoveragePercentageEnabled());

        coverage.setMinCoveragePercentage(true);
        assertTrue(coverage.isMinCoveragePercentageEnabled());
    }

    @Test
    void testCoverageThresholdSetterAndGetter() {
        Coverage coverage = new Coverage();
        assertEquals(0, coverage.getCoverageThreshold());

        coverage.setCoverageThreshold(80);
        assertEquals(80, coverage.getCoverageThreshold());
    }

    @Test
    void testRejectIfLowerSetterAndGetter() {
        Coverage coverage = new Coverage();
        assertFalse(coverage.isRejectIfLowerEnabled());

        coverage.setRejectIfLower(true);
        assertTrue(coverage.isRejectIfLowerEnabled());
    }
}
