package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.application.analyzers.code.CodeMetricsAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.application.analyzers.complexity.ComplexityMetricsAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.application.analyzers.depedency.DependencyAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.*;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IMethodAnalyzerVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JavaMethodVisitor extends JavaParserBaseVisitor<MethodInfo>
        implements IMethodAnalyzerVisitor<ParserRuleContext> {

    private final CodeMetricsAnalyzer codeAnalyzer;
    private final ComplexityMetricsAnalyzer complexityAnalyzer;
    private final DependencyAnalyzer dependencyAnalyzer;

    public JavaMethodVisitor() {
        this.codeAnalyzer = new CodeMetricsAnalyzer();
        this.complexityAnalyzer = new ComplexityMetricsAnalyzer();
        this.dependencyAnalyzer = new DependencyAnalyzer();
    }

    public JavaMethodVisitor(
            CodeMetricsAnalyzer codeAnalyzer,
            ComplexityMetricsAnalyzer complexityAnalyzer,
            DependencyAnalyzer dependencyAnalyzer
    ) {
        this.codeAnalyzer = codeAnalyzer;
        this.complexityAnalyzer = complexityAnalyzer;
        this.dependencyAnalyzer = dependencyAnalyzer;
    }

    @Override
    public MethodInfo visitMethod(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
            String name = methodCtx.identifier().getText();
            String returnType = getReturnType(methodCtx);
            List<String> modifiers = getMethodModifiers(methodCtx);
            List<ParameterInfo> parameters = getParameters(methodCtx);
            ComplexityMetrics complexityMetrics = getComplexityMetrics(methodCtx);
            CodeMetrics codeMetrics = getCodeMetrics(methodCtx);
            DependencyInfo dependencies = getDependencyInfo(methodCtx);

            return new MethodInfo(
                    name,
                    returnType,
                    modifiers,
                    parameters,
                    complexityMetrics,
                    codeMetrics,
                    dependencies
            );
        }, null);
    }

    @Override
    public CodeMetrics getCodeMetrics(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx ->
                        codeAnalyzer.analyze(methodCtx),
                new CodeMetrics(0, 0, 0, 0)
        );
    }

    @Override
    public ComplexityMetrics getComplexityMetrics(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx ->
                        complexityAnalyzer.analyze(methodCtx),
                new ComplexityMetrics(1, 0, 0)
        );
    }

    @Override
    public DependencyInfo getDependencyInfo(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx ->
                        dependencyAnalyzer.analyze(methodCtx),
                new DependencyInfo(new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );
    }

    @Override
    public List<String> getMethodModifiers(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
            List<String> modifiers = new ArrayList<>();

            ParserRuleContext parent = methodCtx.getParent();
            if (parent instanceof JavaParser.MemberDeclarationContext) {
                parent = parent.getParent();
            }

            if (parent instanceof JavaParser.ClassBodyDeclarationContext parentCtx) {
                List<JavaParser.ModifierContext> modifierContexts = parentCtx.modifier();
                if (modifierContexts != null) {
                    for (JavaParser.ModifierContext mod : modifierContexts) {
                        if (mod.classOrInterfaceModifier() != null) {
                            if (mod.classOrInterfaceModifier().PUBLIC() != null) {
                                modifiers.add("public");
                            } else if (mod.classOrInterfaceModifier().PRIVATE() != null) {
                                modifiers.add("private");
                            } else if (mod.classOrInterfaceModifier().PROTECTED() != null) {
                                modifiers.add("protected");
                            } else if (mod.classOrInterfaceModifier().STATIC() != null) {
                                modifiers.add("static");
                            } else if (mod.classOrInterfaceModifier().FINAL() != null) {
                                modifiers.add("final");
                            }
                        }
                    }
                }
            }
            return modifiers;
        }, new ArrayList<>());
    }

    @Override
    public String getReturnType(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx ->
            methodCtx.typeTypeOrVoid().getText(), ""
        );
    }

    @Override
    public List<ParameterInfo> getParameters(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
            List<ParameterInfo> parameters = new ArrayList<>();
            if (methodCtx.formalParameters().formalParameterList() != null) {
                methodCtx.formalParameters()
                        .formalParameterList()
                        .formalParameter()
                        .forEach(param -> {
                            String paramName = param.variableDeclaratorId().getText();
                            String paramType = param.typeType().getText();
                            parameters.add(new ParameterInfo(paramName, paramType));
                        });
            }

            return parameters;
        }, new ArrayList<>());
    }

    private <T> T validateAndExecute(
            ParserRuleContext ctx,
            Function<JavaParser.MethodDeclarationContext, T> operation,
            T defaultValue) {
        if (ctx instanceof JavaParser.MethodDeclarationContext methodCtx) {
            return operation.apply(methodCtx);
        }
        return defaultValue;
    }
}
