package edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedValueMatcher;

import java.util.List;

public interface IDetectionStrategy {
    void detectHardcodedValues(JavaParser.ClassBodyDeclarationContext member,
                               HardcodedValueMatcher matcher, List<HardcodedDetection> detectedValues);
}
