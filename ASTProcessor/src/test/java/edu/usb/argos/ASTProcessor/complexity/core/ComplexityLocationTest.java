package edu.usb.argos.ASTProcessor.complexity.core;

import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexityLocationTest {
    @Test
    void shouldCreateComplexityLocationWithAllFields() {
        ComplexityLocation location = ComplexityLocation.builder()
                .lineNumber(10)
                .complexityType(ComplexityType.IF_STATEMENT)
                .description("If statement found")
                .contextInfo("If statement complexity")
                .build();

        assertEquals(10, location.getLineNumber());
        assertEquals(ComplexityType.IF_STATEMENT, location.getComplexityType());
        assertEquals("If statement found", location.getDescription());
        assertEquals("If statement complexity", location.getContextInfo());
    }
}
