package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedValueMatcher;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DetectorStrategyContext {
    private final List<HardcodedDetection> detectedValues;

    public void execute(IDetectionStrategy strategy, JavaParser.ClassBodyDeclarationContext member) {
        strategy.detectHardcodedValues(member, HardcodedValueMatcher.getInstance(), detectedValues);
    }
}

