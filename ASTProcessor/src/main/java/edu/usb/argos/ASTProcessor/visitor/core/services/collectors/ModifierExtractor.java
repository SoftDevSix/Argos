package edu.usb.argos.ASTProcessor.visitor.core.services.collectors;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ModifierType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ModifierExtractor {
    public List<String> extractModifiers(JavaParser.ClassBodyDeclarationContext ctx) {
        return Optional.ofNullable(ctx)
                .map(JavaParser.ClassBodyDeclarationContext::modifier)
                .map(this::processModifierList)
                .orElse(Collections.emptyList());
    }

    private List<String> processModifierList(List<JavaParser.ModifierContext> modifierContexts) {
        List<String> modifiers = new ArrayList<>();

        for (JavaParser.ModifierContext mod : modifierContexts) {
            processModifier(mod, modifiers);
        }

        return modifiers;
    }

    private void processModifier(JavaParser.ModifierContext mod, List<String> modifiers) {
        JavaParser.ClassOrInterfaceModifierContext classOrInterfaceModifier = mod.classOrInterfaceModifier();
        if (classOrInterfaceModifier == null) {
            return;
        }

        checkAndAddModifiers(classOrInterfaceModifier, modifiers);
    }

    private void checkAndAddModifiers(JavaParser.ClassOrInterfaceModifierContext ctx, List<String> modifiers) {
        for (ModifierType modifierType : ModifierType.values()) {
            if (checkModifierExists(ctx, modifierType.getMethodName())) {
                modifiers.add(modifierType.getKeyword());
                return;
            }
        }
    }

    private boolean checkModifierExists(JavaParser.ClassOrInterfaceModifierContext ctx, String modifierName) {
        try {
            return ctx.getClass().getMethod(modifierName).invoke(ctx) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
