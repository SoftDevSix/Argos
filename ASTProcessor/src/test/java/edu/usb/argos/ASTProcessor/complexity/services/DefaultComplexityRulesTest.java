package edu.usb.argos.ASTProcessor.complexity.services;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.DefaultComplexityRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultComplexityRulesTest {
    private DefaultComplexityRules defaultRules;

    @BeforeEach
    void setUp() {
        defaultRules = new DefaultComplexityRules();
    }

    @Test
    void shouldReturnDefaultMaxComplexity() {
        assertEquals(10, defaultRules.getMaxComplexity());
    }

    @Test
    void shouldHaveComplexityLimitEnabledByDefault() {
        assertTrue(defaultRules.isComplexityLimitEnabled());
    }

    @Test
    void shouldProvideCorrectThresholds() {
        Map<ComplexityLevel, Integer> thresholds = defaultRules.getThresholds();

        assertEquals(5, thresholds.get(ComplexityLevel.LOW));
        assertEquals(10, thresholds.get(ComplexityLevel.MEDIUM));
        assertEquals(15, thresholds.get(ComplexityLevel.HIGH));
        assertEquals(20, thresholds.get(ComplexityLevel.VERY_HIGH));
    }

    @Test
    void shouldProvideImmutableThresholds() {
        Map<ComplexityLevel, Integer> thresholds = defaultRules.getThresholds();

        assertThrows(UnsupportedOperationException.class, () ->
                thresholds.put(ComplexityLevel.LOW, 100));
    }
}
