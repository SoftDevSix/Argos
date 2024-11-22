package edu.usb.argos.ASTProcessor.complexity.core;

import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityResult;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComplexityResultTest {
    @Test
    void shouldCreateComplexityResultWithAllFields() {
        List<ComplexityLocation> locations = Arrays.asList(
                ComplexityLocation.builder()
                        .lineNumber(10)
                        .complexityType(ComplexityType.IF_STATEMENT)
                        .build()
        );

        ComplexityResult result = ComplexityResult.builder()
                .elementName("TestMethod")
                .complexityScore(5)
                .complexityLevel(ComplexityLevel.LOW)
                .isWithinLimits(true)
                .complexityLocations(locations)
                .build();

        assertEquals("TestMethod", result.getElementName());
        assertEquals(5, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
        assertTrue(result.isWithinLimits());
        assertEquals(1, result.getComplexityLocations().size());
    }
}
