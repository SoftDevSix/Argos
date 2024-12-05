package edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection.HardcodedDetectionContext;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection.IDetectionStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HardcodedDetectionContextTest {
    @Test
    void testExecuteDetectHardcodedValues() {
        List<HardcodedDetection> detectedValues = new ArrayList<>();

        IDetectionStrategy strategy = (member, matcher, detectedValues1) -> {
            detectedValues1.add(HardcodedDetection.builder()
                    .hardcodedValue("\"Hardcoded String\"")
                    .lineNumber(10)
                    .build());
        };

        JavaParser.ClassBodyDeclarationContext classBodyContext =
                new JavaParser.ClassBodyDeclarationContext(null, 0);
        HardcodedDetectionContext context = new HardcodedDetectionContext(detectedValues);
        context.execute(strategy, classBodyContext);

        assertEquals(1, detectedValues.size());
        assertEquals("\"Hardcoded String\"", detectedValues.get(0).getHardcodedValue());
        assertEquals(10, detectedValues.get(0).getLineNumber());
    }
}
