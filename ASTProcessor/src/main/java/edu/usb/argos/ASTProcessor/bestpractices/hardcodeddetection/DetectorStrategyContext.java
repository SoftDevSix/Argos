package edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedValueMatcher;

import java.util.List;

public class DetectorStrategyContext {
    private final HardcodedValueMatcher matcher;
    private final List<String> detectedValues;

    public DetectorStrategyContext(HardcodedValueMatcher matcher, List<String> detectedValues) {
        this.matcher = matcher;
        this.detectedValues = detectedValues;
    }

    public void execute(IDetectionStrategy strategy, JavaParser.ClassBodyDeclarationContext member) {
        strategy.detectHardcodedValues(member, matcher, detectedValues);
    }
}

