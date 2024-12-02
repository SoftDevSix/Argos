package edu.usb.argos.astprocessor.analyzer.infrastructure.utils;

import java.util.Optional;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import org.antlr.v4.runtime.ParserRuleContext;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;

public class ExcessiveParametersMethodAnalyzer implements IMethodLineAnalyzer<JavaParser.StatementContext> {
    @Override
    public int calculateMethodSize(MethodInformation<JavaParser.StatementContext> method) {
        if (method.getStatements() == null || method.getStatements().isEmpty()) {
            return 0;
        }

        Optional<JavaParser.MethodDeclarationContext> methodContext = findEnclosingMethod(method.getStatements()
                .get(0));

        if (methodContext.isEmpty()) {
            return 0;
        }

        Optional<JavaParser.BlockContext> methodBody = Optional.ofNullable(methodContext.get().methodBody().block());
        return methodBody
                .map(blockContext -> blockContext.getStop().getLine() - blockContext.getStart().getLine())
                .orElse(0);
    }

     @Override
    public int getMethodStartLine(MethodInformation<JavaParser.StatementContext> method) {
        if (method.getStatements() == null || method.getStatements().isEmpty()) {
            return 0;
        }

        Optional<JavaParser.MethodDeclarationContext> methodContext =
                findEnclosingMethod(method.getStatements().get(0));

        return methodContext
                .map(methodNode -> methodNode.getStart().getLine())
                .orElse(0);
    }
    
    @Override
    public int getMethodEndLine(MethodInformation<JavaParser.StatementContext> method) {
        if (method.getStatements() == null || method.getStatements().isEmpty()) {
            return 0;
        }

        Optional<JavaParser.MethodDeclarationContext> methodContext = findEnclosingMethod(method.getStatements()
                .get(0));

        return methodContext
                .map(methodNode -> methodNode.getStop().getLine())
                .orElse(0);
    }

    private Optional<JavaParser.MethodDeclarationContext> findEnclosingMethod(ParserRuleContext context) {
        ParserRuleContext currentContext = context;

        while (currentContext != null) {
            if (currentContext instanceof JavaParser.MethodDeclarationContext) {
                return Optional.of((JavaParser.MethodDeclarationContext) currentContext);
            }

            currentContext = currentContext.getParent();
        }

        return Optional.empty();
    }
}
