package edu.usb.argos.ASTProcessor.visitor.domain.services.collectors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.classes.*;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes.IClassIdentityCollector;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.*;

public class JavaClassIdentityCollector implements IClassIdentityCollector<ParserRuleContext> {
    private final CommonTokenStream tokenStream;

    public JavaClassIdentityCollector(CommonTokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    @Override
    public String getClassName(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> classCtx.identifier().getText(),
                ""
        );
    }

    @Override
    public String getPackageName(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.PackageDeclarationContext.class,
                packageCtx -> packageCtx.qualifiedName().getText(),
                ""
        );
    }

    @Override
    public List<String> getClassModifiers(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> {
                    List<String> modifiers = new ArrayList<>();
                    if (classCtx.parent instanceof JavaParser.ClassBodyDeclarationContext) {
                        JavaParser.ClassBodyDeclarationContext parent =
                                (JavaParser.ClassBodyDeclarationContext) classCtx.parent;
                        parent.modifier().forEach(mod -> modifiers.add(mod.getText()));
                    }
                    return modifiers;
                },
                new ArrayList<>()
        );
    }

    @Override
    public List<AnnotationInfo> getClassAnnotations(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> {
                    List<AnnotationInfo> annotations = new ArrayList<>();
                    if (classCtx.parent instanceof JavaParser.ClassBodyDeclarationContext) {
                        JavaParser.ClassBodyDeclarationContext parent =
                                (JavaParser.ClassBodyDeclarationContext) classCtx.parent;

                        parent.modifier().stream()
                                .filter(mod -> mod.classOrInterfaceModifier().annotation() != null)
                                .forEach(mod -> {
                                    String name = mod.classOrInterfaceModifier().annotation().qualifiedName().getText();
                                    Map<String, Object> attributes = new HashMap<>();

                                    if (mod.classOrInterfaceModifier().annotation().elementValuePairs() != null) {
                                        mod.classOrInterfaceModifier().annotation().elementValuePairs()
                                                .elementValuePair().forEach(pair -> {
                                                    String attributeName = pair.identifier().getText();
                                                    String attributeValue = pair.elementValue().getText();
                                                    attributes.put(attributeName, attributeValue);
                                                });
                                    }

                                    annotations.add(new AnnotationInfo(name, attributes));
                                });
                    }
                    return annotations;
                },
                new ArrayList<>()
        );
    }
}
