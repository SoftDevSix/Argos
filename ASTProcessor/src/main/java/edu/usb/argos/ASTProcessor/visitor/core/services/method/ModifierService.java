package edu.usb.argos.ASTProcessor.visitor.core.services.method;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.services.IModifierExtractor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Value
public class ModifierService implements IModifierExtractor<JavaParser.ClassBodyDeclarationContext> {
    private static final String ANNOTATION_PREFIX = "@";

    @Override
    public Optional<List<String>> extractModifiers(JavaParser.ClassBodyDeclarationContext bodyCtx) {
        if (bodyCtx == null) {
            return Optional.empty();
        }
        List<String> modifiers = processModifiers(bodyCtx);
        return Optional.of(modifiers);
    }

    private List<String> processModifiers(JavaParser.ClassBodyDeclarationContext bodyCtx) {
        return Optional.ofNullable(bodyCtx.modifier())
                .map(this::extractModifiersFromList)
                .orElse(Collections.emptyList());
    }

    private List<String> extractModifiersFromList(List<JavaParser.ModifierContext> modifiers) {
        List<String> extractedModifiers = new ArrayList<>();
        for (JavaParser.ModifierContext mod : modifiers) {
            extractModifier(mod).
                    ifPresent(extractedModifiers::add);
        }
        return filterAndMakeUnmodifiable(extractedModifiers);
    }

    private Optional<String> extractModifier(JavaParser.ModifierContext mod) {
        if (mod.classOrInterfaceModifier() == null) {
            return Optional.of(mod.getText());
        }
        return Optional.of(mod.classOrInterfaceModifier().getText());
    }

    private List<String> filterAndMakeUnmodifiable(List<String> modifiers) {
        List<String> filteredModifiers = new ArrayList<>();
        for (String mod : modifiers) {
            if (!mod.startsWith(ANNOTATION_PREFIX)) {
                filteredModifiers.add(mod);
            }
        }
        return Collections.unmodifiableList(filteredModifiers);
    }
}
