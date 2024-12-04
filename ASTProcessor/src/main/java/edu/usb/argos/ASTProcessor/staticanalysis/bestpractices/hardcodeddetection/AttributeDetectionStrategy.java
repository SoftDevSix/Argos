package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.hardcodeddetection;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedDetection;
import edu.usb.argos.ASTProcessor.staticanalysis.bestpractices.HardcodedValueMatcher;

import java.util.List;
import java.util.stream.Stream;

public class AttributeDetectionStrategy implements IDetectionStrategy {
    private final String FINAL_MODIFIER = "final";

    @Override
    public void detectHardcodedValues(JavaParser.ClassBodyDeclarationContext member,
                                      HardcodedValueMatcher matcher, List<HardcodedDetection> detectedValues) {
        if (isFieldDeclaration(member)) {
            JavaParser.FieldDeclarationContext field = member.memberDeclaration().fieldDeclaration();
            Stream<String> modifiers = extractModifiers(member);

            detectHardcodedAttributes(field, modifiers, matcher, detectedValues);
        }
    }

    private boolean isFieldDeclaration(JavaParser.ClassBodyDeclarationContext member) {
        return member.memberDeclaration() != null &&
                member.memberDeclaration().fieldDeclaration() != null;
    }

    private Stream<String> extractModifiers(JavaParser.ClassBodyDeclarationContext member) {
        return member.modifier().stream().map(JavaParser.ModifierContext::getText);
    }

    private void detectHardcodedAttributes(JavaParser.FieldDeclarationContext field,
                                           Stream<String> modifiers,
                                           HardcodedValueMatcher matcher,
                                           List<HardcodedDetection> detectedValues) {
        for (JavaParser.VariableDeclaratorContext declarator : field.variableDeclarators().variableDeclarator()) {
            if (declarator.variableInitializer() != null) {
                String attributeValue = declarator.variableInitializer().getText();
                if (modifiers.noneMatch(mod -> mod.equals(FINAL_MODIFIER)) &&
                        matcher.isHardcoded(attributeValue)) {
                    HardcodedDetection detection = HardcodedDetection.builder()
                            .hardcodedValue(attributeValue)
                            .lineNumber(declarator.getStart().getLine())
                            .build();
                    detectedValues.add(detection);
                }
            }
        }
    }
}