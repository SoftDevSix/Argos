package edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes;

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
                    if (ctx.parent instanceof JavaParser.TypeDeclarationContext) {
                        JavaParser.TypeDeclarationContext typeCtx = (JavaParser.TypeDeclarationContext) ctx.parent;
                        if (typeCtx.classOrInterfaceModifier() != null) {
                            for (JavaParser.ClassOrInterfaceModifierContext mod : typeCtx.classOrInterfaceModifier()) {
                                if (mod.getText() != null) {
                                    modifiers.add(mod.getText());
                                }
                            }
                        }
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
                    if (ctx.parent instanceof JavaParser.TypeDeclarationContext) {
                        JavaParser.TypeDeclarationContext typeCtx = (JavaParser.TypeDeclarationContext) ctx.parent;
                        if (typeCtx.classOrInterfaceModifier() != null) {
                            for (JavaParser.ClassOrInterfaceModifierContext mod : typeCtx.classOrInterfaceModifier()) {
                                if (mod.annotation() != null) {
                                    AnnotationInfo annotation = new AnnotationInfo();
                                    annotation.setName(mod.annotation().qualifiedName().getText());

                                    if (mod.annotation().elementValuePairs() != null) {
                                        Map<String, String> attributes = new HashMap<>();
                                        for (JavaParser.ElementValuePairContext pair : mod.annotation().elementValuePairs().elementValuePair()) {
                                            attributes.put(pair.identifier().getText(),
                                                    pair.elementValue().getText().replace("\"", ""));
                                        }
                                        annotation.setAttributes(attributes);
                                    } else {
                                        annotation.setAttributes(new HashMap<>());
                                    }

                                    annotations.add(annotation);
                                }
                            }
                        }
                    }
                    return annotations;
                },
                new ArrayList<>()
        );
    }
}
