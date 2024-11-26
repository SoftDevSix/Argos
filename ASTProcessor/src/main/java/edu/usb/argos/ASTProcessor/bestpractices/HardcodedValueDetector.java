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
    private final List<HardcodedDetection> hardcodedValues;
    private final JavaParser.ClassDeclarationContext classContext;

    public HardcodedValueDetector(JavaParser.ClassDeclarationContext classContext) {
        this.classContext = classContext;
        hardcodedValues = new ArrayList<>();
    }

    public void detectHardcodedValues() {
        DetectorStrategyContext strategyContext = new DetectorStrategyContext(hardcodedValues);

        for (JavaParser.ClassBodyDeclarationContext member : classContext.classBody().classBodyDeclaration()) {
            strategyContext.execute(new AttributeDetectionStrategy(), member);
            strategyContext.execute(new ConstructorDetectionStrategy(), member);
            strategyContext.execute(new MethodDetectionStrategy(), member);
        }
    }
}
