package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedValueMatcher;

import java.util.List;

public interface IDetectionStrategy {
    void detectHardcodedValues(JavaParser.ClassBodyDeclarationContext member,
                               HardcodedValueMatcher matcher, List<HardcodedDetection> detectedValues);
}
