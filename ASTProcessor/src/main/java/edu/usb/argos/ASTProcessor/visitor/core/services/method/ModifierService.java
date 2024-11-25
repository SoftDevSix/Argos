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
    @Override
    public List<String> extractModifiers(JavaParser.ClassBodyDeclarationContext bodyCtx) {
        return Optional.ofNullable(bodyCtx)
                .map(this::processModifiers)
                .orElse(Collections.emptyList());
    }

    private List<String> processModifiers(JavaParser.ClassBodyDeclarationContext bodyCtx) {
        return Optional.ofNullable(bodyCtx.modifier())
                .map(this::extractModifiersFromList)
                .orElse(Collections.emptyList());
    }

    private List<String> extractModifiersFromList(List<JavaParser.ModifierContext> modifiers) {
        List<String> extractedModifiers = new ArrayList<>();
        for (JavaParser.ModifierContext mod : modifiers) {
            String modifier = extractModifier(mod);
            if (modifier != null) {
                extractedModifiers.add(modifier);
            }
        }
        return filterAndMakeUnmodifiable(extractedModifiers);
    }

    private String extractModifier(JavaParser.ModifierContext mod) {
        if (mod.classOrInterfaceModifier() != null) {
            return mod.classOrInterfaceModifier().getText();
        }
        return mod.getText();
    }

    private List<String> filterAndMakeUnmodifiable(List<String> modifiers) {
        List<String> filteredModifiers = new ArrayList<>();
        for (String mod : modifiers) {
            if (!mod.startsWith("@")) {
                filteredModifiers.add(mod);
            }
        }
        return Collections.unmodifiableList(filteredModifiers);
    }
}
