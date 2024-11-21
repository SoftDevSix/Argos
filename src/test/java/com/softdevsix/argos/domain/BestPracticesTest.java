package com.softdevsix.argos.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BestPracticesTest {
    @Test
    void testNoHardcodedValuesSetterAndGetter() {
        BestPractices_rules bestPractices = new BestPractices_rules();

        assertFalse(bestPractices.isNoHardcodedValuesEnabled());

        bestPractices.setNoHardcodedValues(true);
        assertTrue(bestPractices.isNoHardcodedValuesEnabled());

        bestPractices.setNoHardcodedValues(false);
        assertFalse(bestPractices.isNoHardcodedValuesEnabled());
    }
}
