package edu.usb.argos.ASTProcessor.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestpractices.HardcodedValueMatcher;

import java.util.List;
import java.util.stream.Stream;

public class AttributeDetectionStrategy implements IDetectionStrategy {
    @Override
    public void detectHardcodedValues(JavaParser.ClassBodyDeclarationContext member,
                                      HardcodedValueMatcher matcher, List<String> detectedValues) {
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().fieldDeclaration() != null) {
            JavaParser.FieldDeclarationContext field = member.memberDeclaration().fieldDeclaration();
            Stream<String> modifiers = member.modifier().stream().map(JavaParser.ModifierContext::getText);

            for (JavaParser.VariableDeclaratorContext declarator : field.variableDeclarators().variableDeclarator()) {
                if (declarator.variableInitializer() != null) {
                    String attributeValue = declarator.variableInitializer().getText();
                    if (modifiers.noneMatch(mod -> mod.equals("final")) && matcher.isHardcoded(attributeValue)) {
                        System.out.println("Hardcoded value at line " + declarator.getStart().getLine() + ": " + attributeValue);
                        detectedValues.add(attributeValue);
                    }
                }
            }
        }
    }
}

