package edu.usb.argos.ASTProcessor.visitor.domain.services.collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;

public class ModifierExtractor {
    private static final Map<String, String> MODIFIER_MAP = Map.of(
            "PUBLIC", "public",
            "PRIVATE", "private",
            "PROTECTED", "protected",
            "STATIC", "static",
            "FINAL", "final"
    );

    public List<String> extractModifiers(JavaParser.ClassBodyDeclarationContext ctx) {
        List<String> modifiers = new ArrayList<>();

        List<JavaParser.ModifierContext> modifierContexts = ctx.modifier();
        if (modifierContexts == null) {
            return modifiers;
        }

        for (JavaParser.ModifierContext mod : modifierContexts) {
            processModifier(mod, modifiers);
        }

        return modifiers;
    }

    private void processModifier(JavaParser.ModifierContext mod, List<String> modifiers) {
        if (mod.classOrInterfaceModifier() == null) {
            return;
        }

        for (Map.Entry<String, String> entry : MODIFIER_MAP.entrySet()) {
            if (hasModifier(mod.classOrInterfaceModifier(), entry.getKey())) {
                modifiers.add(entry.getValue());
                break;
            }
        }
    }

    private boolean hasModifier(JavaParser.ClassOrInterfaceModifierContext ctx, String modifierName) {
        try {
            return ctx.getClass().getMethod(modifierName).invoke(ctx) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
