package edu.usb.argos.ASTProcessor.bestpractices;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection.AttributeDetectionStrategy;
import edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection.ConstructorDetectionStrategy;
import edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection.DetectorStrategyContext;
import edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection.MethodDetectionStrategy;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HardcodedValueDetector {
    private final HardcodedValueMatcher hardcodedValueMatcher;
    private final List<String> hardcodedValues = new ArrayList<>();
    private final JavaParser.ClassDeclarationContext classContext;

    public HardcodedValueDetector(JavaParser.ClassDeclarationContext classContext) {
        this.classContext = classContext;
        this.hardcodedValueMatcher = HardcodedValueMatcher.getInstance();
    }

    public void detectHardcodedValues() {
        DetectorStrategyContext strategyContext = new DetectorStrategyContext(hardcodedValueMatcher, hardcodedValues);

        for (JavaParser.ClassBodyDeclarationContext member : classContext.classBody().classBodyDeclaration()) {
            strategyContext.execute(new AttributeDetectionStrategy(), member);
            strategyContext.execute(new ConstructorDetectionStrategy(), member);
            strategyContext.execute(new MethodDetectionStrategy(), member);
        }
    }
}
