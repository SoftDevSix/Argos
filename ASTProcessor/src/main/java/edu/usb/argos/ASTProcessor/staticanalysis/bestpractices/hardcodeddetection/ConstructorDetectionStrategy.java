package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection;


import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedValueMatcher;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.analyzer.BlockAnalyzer;
import lombok.Value;

import java.util.List;

@Value
public class ConstructorDetectionStrategy implements IDetectionStrategy {
    BlockAnalyzer analyzer;

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