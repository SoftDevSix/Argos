package edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection;


import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedValueMatcher;
import edu.usb.argos.ASTProcessor.bestpractices.analyzer.BlockAnalyzer;

import java.util.List;

public class ConstructorDetectionStrategy implements IDetectionStrategy {
    private final BlockAnalyzer analyzer;

    public ConstructorDetectionStrategy(){
        analyzer = new BlockAnalyzer();
    }

    @Override
    public void detectHardcodedValues(JavaParser.ClassBodyDeclarationContext member,
                                      HardcodedValueMatcher matcher, List<HardcodedDetection> detectedValues) {
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().constructorDeclaration() != null) {
            JavaParser.ConstructorDeclarationContext constructor = member.memberDeclaration().constructorDeclaration();
            analyzer.analyze(constructor.block(), detectedValues);
        }
    }
}

