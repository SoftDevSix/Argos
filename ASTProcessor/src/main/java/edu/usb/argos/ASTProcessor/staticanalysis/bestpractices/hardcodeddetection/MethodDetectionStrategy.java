package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection;


import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedValueMatcher;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.analyzer.BlockAnalyzer;

import java.util.List;

public class MethodDetectionStrategy implements IDetectionStrategy {
    private final BlockAnalyzer analyzer;

    public MethodDetectionStrategy(){
        analyzer = new BlockAnalyzer();
    }

    @Override
    public void detectHardcodedValues(JavaParser.ClassBodyDeclarationContext member,
                                      HardcodedValueMatcher matcher, List<HardcodedDetection> detectedValues) {
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().methodDeclaration() != null) {
            JavaParser.MethodDeclarationContext method = member.memberDeclaration().methodDeclaration();
            analyzer.analyze(method.methodBody().block(), detectedValues);
        }
    }
}
