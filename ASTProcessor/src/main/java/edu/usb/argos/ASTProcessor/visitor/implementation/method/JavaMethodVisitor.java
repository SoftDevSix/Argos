package edu.usb.argos.ASTProcessor.visitor.implementation.method;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.interfaces.IMethodAnalyzerVisitor;
import edu.usb.argos.ASTProcessor.visitor.models.method.MethodInfo;
import edu.usb.argos.ASTProcessor.visitor.models.method.ParameterInfo;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JavaMethodVisitor extends JavaParserBaseVisitor<MethodInfo> implements IMethodAnalyzerVisitor {
    @Override
    public MethodInfo visitMethod(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
            String name = methodCtx.identifier().getText();
            String returnType = getReturnType(methodCtx);
            List<String> modifiers = getMethodModifiers(methodCtx);
            List<ParameterInfo> parameters = getParameters(methodCtx);
            int lines = getMethodLines(methodCtx);

            return new MethodInfo(name, returnType, modifiers, parameters, lines);
        }, null);
    }

    @Override
    public int getMethodLines(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
            Token start = methodCtx.getStart();
            Token stop = methodCtx.getStop();

            return stop.getLine() - start.getLine() + 1;
        }, 0);
    }

    @Override
    public List<String> getMethodModifiers(ParserRuleContext ctx) {
        return validateAndExecute(ctx, methodCtx -> {
            List<String> modifiers = new ArrayList<>();
            if (methodCtx.getParent() instanceof JavaParser.ClassBodyDeclarationContext parentCtx) {
                if (parentCtx.modifier() != null) {
                    parentCtx.modifier().forEach(mod -> modifiers.add(mod.getText()));
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
