package edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection;


import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedValueMatcher;
import edu.usb.argos.ASTProcessor.bestpractices.analyzer.BlockAnalyzer;

import java.util.List;

public class MethodDetectionStrategy implements IDetectionStrategy {
    @Override
    public void detectHardcodedValues(JavaParser.ClassBodyDeclarationContext member,
                                      HardcodedValueMatcher matcher, List<HardcodedDetection> detectedValues) {
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().methodDeclaration() != null) {
            JavaParser.MethodDeclarationContext method = member.memberDeclaration().methodDeclaration();
            BlockAnalyzer.getInstance().analyze(method.methodBody().block(), detectedValues);
        }
    }
}

