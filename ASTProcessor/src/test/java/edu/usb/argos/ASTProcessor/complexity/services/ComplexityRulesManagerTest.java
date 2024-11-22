package edu.usb.argos.ASTProcessor.complexity.services;

import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.rules.ComplexityRulesProvider;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.ComplexityRulesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ComplexityRulesManagerTest {
    @Mock
    private ComplexityRulesProvider rulesProvider;

    private ComplexityRulesManager rulesManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rulesManager = new ComplexityRulesManager();
    }

    @Test
    void shouldUseDefaultRulesProviderWhenNotSet() {
        assertTrue(rulesManager.isComplexityLimitEnabled());
        assertEquals(10, rulesManager.getMaxComplexity());
    }

    @Test
    void shouldUseCustomRulesProviderWhenSet() {
        when(rulesProvider.isComplexityLimitEnabled()).thenReturn(false);
        when(rulesProvider.getMaxComplexity()).thenReturn(15);

        rulesManager.setRulesProvider(rulesProvider);

        assertFalse(rulesManager.isComplexityLimitEnabled());
        assertEquals(15, rulesManager.getMaxComplexity());
    }

    @Test
    void shouldDetermineLevelCorrectly() {
        when(rulesProvider.getThresholds()).thenReturn(Map.of(
                ComplexityLevel.LOW, 5,
                ComplexityLevel.MEDIUM, 10,
                ComplexityLevel.HIGH, 15,
                ComplexityLevel.VERY_HIGH, 20
        ));

        rulesManager.setRulesProvider(rulesProvider);

        assertEquals(ComplexityLevel.LOW, rulesManager.determineLevel(4));
        assertEquals(ComplexityLevel.MEDIUM, rulesManager.determineLevel(8));
        assertEquals(ComplexityLevel.HIGH, rulesManager.determineLevel(12));
        assertEquals(ComplexityLevel.VERY_HIGH, rulesManager.determineLevel(16));
    }
}
