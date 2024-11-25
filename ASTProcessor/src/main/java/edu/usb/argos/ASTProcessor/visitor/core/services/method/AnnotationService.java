package edu.usb.argos.ASTProcessor.visitor.core.services.method;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.services.IAnnotationExtractor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Value
public class AnnotationService implements IAnnotationExtractor<JavaParser.ClassBodyDeclarationContext> {
    @Override
    public List<String> extractAnnotation(JavaParser.ClassBodyDeclarationContext bodyCtx) {
        return Optional.ofNullable(bodyCtx)
                .map(this::processModifiers)
                .orElse(Collections.emptyList());
    }

    private List<String> processModifiers(JavaParser.ClassBodyDeclarationContext bodyCtx) {
        return Optional.ofNullable(bodyCtx.modifier())
                .map(this::extractAnnotationsFromModifiers)
                .orElse(Collections.emptyList());
    }

    private List<String> extractAnnotationsFromModifiers(List<JavaParser.ModifierContext> modifiers) {
        List<String> annotations = new ArrayList<>();
        for (JavaParser.ModifierContext mod : modifiers) {
            String annotation = extractAnnotationFromModifier(mod).orElse(null);
            if (annotation != null) {
                annotations.add(annotation);
            }
        }
        return Collections.unmodifiableList(annotations);
    }

    private Optional<String> extractAnnotationFromModifier(JavaParser.ModifierContext mod) {
        return Optional.ofNullable(mod.classOrInterfaceModifier())
                .map(JavaParser.ClassOrInterfaceModifierContext::annotation)
                .map(JavaParser.AnnotationContext::getText);
    }
}
