package edu.usb.argos.astprocessor.visitor.core.services.classes;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.AnnotationInformation;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes.IClassIdentityService;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.shared.validation.ContextValidator;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

public class JavaClassIdentityService implements IClassIdentityService<ParserRuleContext> {

    private static final String DEFAULT_VALUE = "";

    @Override
    public Optional<String> getClassName(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> Optional.ofNullable(classCtx.identifier().getText()),
                Optional.empty()
        );
    }

    @Override
    public Optional<String> getPackageName(ParserRuleContext ctx) {
        return Optional.ofNullable(findCompilationUnitContext(ctx))
                .flatMap(this::extractPackageName);
    }

    private ParserRuleContext findCompilationUnitContext(ParserRuleContext ctx) {
        ParserRuleContext current = ctx;
        while (current != null && !(current instanceof JavaParser.CompilationUnitContext)) {
            current = current.getParent();
        }
        return current;
    }

    private Optional<String> extractPackageName(ParserRuleContext compilationUnitContext) {
        if (compilationUnitContext instanceof JavaParser.CompilationUnitContext) {
            return ContextValidator.validateAndExecute(
                    ((JavaParser.CompilationUnitContext) compilationUnitContext).packageDeclaration(),
                    JavaParser.PackageDeclarationContext.class,
                    packageCtx -> Optional.ofNullable(packageCtx.qualifiedName().getText()),
                    Optional.empty()
            );
        }
        return Optional.empty();
    }

    @Override
    public List<String> getClassModifiers(ParserRuleContext ctx) {
        JavaParser.TypeDeclarationContext typeCtx = getTypeDeclarationContext(ctx);
        return typeCtx != null ? extractModifiers(typeCtx) : Collections.emptyList();
    }

    private JavaParser.TypeDeclarationContext getTypeDeclarationContext(ParserRuleContext ctx) {
        if (ctx.parent instanceof JavaParser.TypeDeclarationContext) {
            return (JavaParser.TypeDeclarationContext) ctx.parent;
        }
        return null;
    }

    private List<String> extractModifiers(JavaParser.TypeDeclarationContext typeCtx) {
        List<String> modifiers;
        modifiers = new ArrayList<>();
        for (JavaParser.ClassOrInterfaceModifierContext mod : getClassOrInterfaceModifiers(typeCtx)) {
            String modifierText = getModifierText(mod);
            if (!modifierText.isEmpty()) {
                modifiers.add(modifierText);
            }
        }
        return modifiers;
    }

    private List<JavaParser.ClassOrInterfaceModifierContext> getClassOrInterfaceModifiers(
            JavaParser.TypeDeclarationContext typeCtx) {
        return typeCtx.classOrInterfaceModifier() != null ? typeCtx.classOrInterfaceModifier() : Collections.emptyList();
    }

    private String getModifierText(JavaParser.ClassOrInterfaceModifierContext mod) {
        return mod.getText() != null ? mod.getText() : DEFAULT_VALUE;
    }

    @Override
    public List<AnnotationInformation> getClassAnnotations(ParserRuleContext ctx) {
        JavaParser.TypeDeclarationContext typeCtx = getTypeDeclarationContext(ctx);
        return typeCtx != null ? extractAnnotations(typeCtx) : Collections.emptyList();
    }

    private List<AnnotationInformation> extractAnnotations(JavaParser.TypeDeclarationContext typeCtx) {
        List<AnnotationInformation> annotations = new ArrayList<>();
        for (JavaParser.ClassOrInterfaceModifierContext mod : getClassOrInterfaceModifiers(typeCtx)) {
            if (mod.annotation() != null) {
                annotations.add(createAnnotationInfo(mod));
            }
        }
        return annotations;
    }

    private AnnotationInformation createAnnotationInfo(JavaParser.ClassOrInterfaceModifierContext mod) {
        return AnnotationInformation.builder()
                .name(getAnnotationName(mod))
                .attributes(getAnnotationAttributes(mod))
                .build();
    }

    private String getAnnotationName(JavaParser.ClassOrInterfaceModifierContext mod) {
        return mod.annotation().qualifiedName().getText();
    }

    private Map<String, String> getAnnotationAttributes(JavaParser.ClassOrInterfaceModifierContext mod) {
        Map<String, String> attributes = new HashMap<>();
        if (mod.annotation().elementValuePairs() != null) {
            for (JavaParser.ElementValuePairContext pair : mod.annotation().elementValuePairs().elementValuePair()) {
                attributes.put(pair.identifier().getText(), getAttributeValue(pair));
            }
        }
        return attributes;
    }

    private String getAttributeValue(JavaParser.ElementValuePairContext pair) {
        return pair.elementValue().getText().replace("\"", DEFAULT_VALUE);
    }
}
