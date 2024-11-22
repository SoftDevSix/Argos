package edu.usb.argos.ASTProcessor.bestPractices;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrExpressionAdapter;

import java.util.List;

public class HardcodedValueDetector {

    private HardcodedValueMatcher hardcodedValueMatcher;

    public HardcodedValueDetector() {
        this.hardcodedValueMatcher = HardcodedValueMatcher.getInstance();
    }

    public void detectHardcodedValues(ClassInformation classInformation) {
        detectHardcodedValuesInAttributes(classInformation.getMembers().getAttributes());
        detectHardcodedValuesInMethods(classInformation.getMembers().getMethods());
        detectHardcodedValuesInContructor(classInformation.getMembers().getConstructors());
    }

    private void detectHardcodedValuesInAttributes(List<AttributeInformation> attributes) {
        for (AttributeInformation attribute : attributes) {
            System.out.println(attribute.getName());
        }
    }

    private void detectHardcodedValuesInContructor(List<ConstructorInformation> constructors) {
        for (ConstructorInformation constructor : constructors) {
            constructor.getBodyStatements().forEach(statement -> {
                JavaParser.BlockStatementContext statementContext = (JavaParser.BlockStatementContext) statement;
                String statementText = statementContext.getText();
                if (hardcodedValueMatcher.isHardcoded(statementText)) {
                    System.out.println("Hardcoded value in expression: " + statementText);
                }
            });
        }
    }

    private void detectHardcodedValuesInMethods(List<MethodInformation> methods) {
        for (MethodInformation method : methods) {
            method.getExpressions().forEach(expression -> {
                JavaParser.ExpressionContext expressionContext = ((AntlrExpressionAdapter) expression).getNode();
                String expressionText = expressionContext.getText();
                if (hardcodedValueMatcher.isHardcoded(expressionText)) {
                    System.out.println("Hardcoded value in expression: " + expressionText);
                }
            });
        }
    }
}
