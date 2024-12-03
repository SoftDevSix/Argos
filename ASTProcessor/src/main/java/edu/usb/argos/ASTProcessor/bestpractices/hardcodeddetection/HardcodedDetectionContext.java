package edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedValueMatcher;
import lombok.Value;

import java.util.List;

@Value
public class HardcodedDetectionContext {
    List<HardcodedDetection> detectedValues;

    public void execute(IDetectionStrategy strategy, JavaParser.ClassBodyDeclarationContext member) {
        strategy.detectHardcodedValues(member, HardcodedValueMatcher.getInstance(), detectedValues);
    }
}

