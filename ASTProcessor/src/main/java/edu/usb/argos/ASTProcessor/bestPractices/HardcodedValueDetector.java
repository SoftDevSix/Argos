package edu.usb.argos.ASTProcessor.bestPractices;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrExpressionAdapter;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters.AntlrStatementAdapter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HardcodedValueDetector {
    private final HardcodedValueMatcher hardcodedValueMatcher;
    private final List<String> hardcodedValues;

    public HardcodedValueDetector() {
        this.hardcodedValueMatcher = HardcodedValueMatcher.getInstance();
        this.hardcodedValues = new ArrayList<>();
    }

    public void detectHardcodedValues(ClassInformation classInformation) {
        detectHardcodedValuesInAttributes(classInformation.getMembers().getAttributes());
        detectHardcodedValuesInMethods(classInformation.getMembers().getMethods());
        detectHardcodedValuesInConstructors(classInformation.getMembers().getConstructors());
    }

    private void detectHardcodedValuesInAttributes(List<AttributeInformation> attributes) {
        for (AttributeInformation attribute : attributes) {
            System.out.println(attribute.getName());
        }
    }

    private void detectHardcodedValuesInConstructors(List<ConstructorInformation> constructors) {
        for (ConstructorInformation constructor : constructors) {
            constructor.getBodyStatements().forEach(statement -> {
                JavaParser.BlockStatementContext statementContext = (JavaParser.BlockStatementContext) statement;
                String statementText = statementContext.getText();
                if (hardcodedValueMatcher.isHardcoded(statementText)) {
                    hardcodedValues.add(statementText);
                }
            });
        }
    }

    private void detectHardcodedValuesInMethods(List<MethodInformation> methods) {
        for (MethodInformation method : methods) {
            method.getStatements().forEach(st -> {
                JavaParser.StatementContext statementContext = ((AntlrStatementAdapter) st).getNode();
                String statementText = statementContext.getText();
                System.out.println(statementText);
                if (hardcodedValueMatcher.isHardcoded(statementText)) {
                    hardcodedValues.add(statementText);
                }
            });
            method.getExpressions().forEach(expression -> {
                JavaParser.ExpressionContext expressionContext = ((AntlrExpressionAdapter) expression).getNode();
                String expressionText = expressionContext.getText();
                if (hardcodedValueMatcher.isHardcoded(expressionText)) {
                    hardcodedValues.add(expressionText);
                }
            });
        }
    }
}
