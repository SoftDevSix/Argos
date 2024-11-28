package edu.usb.argos.astprocessor.analyzer.infrastructure.utils;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MethodLineAnalyzer implements IMethodLineAnalyzer<JavaParser.StatementContext> {

    @Override
    public int calculateMethodSize(MethodInformation<JavaParser.StatementContext> method) {
        if (isMethodEmpty(method)) {
            return 0;
        }
        int startLine = getMethodStartLine(method);
        int endLine = getMethodEndLine(method);
        return endLine - startLine + 1;
    }

    @Override
    public int getMethodStartLine(MethodInformation<JavaParser.StatementContext> method) {
        return calculateLine(method.getStatements(), true);
    }

    @Override
    public int getMethodEndLine(MethodInformation<JavaParser.StatementContext> method) {
        return calculateLine(method.getStatements(), false);
    }

    private boolean isMethodEmpty(MethodInformation<JavaParser.StatementContext> method) {
        return method.getStatements() == null || method.getStatements().isEmpty();
    }

    private int calculateLine(List<JavaParser.StatementContext> statements, boolean findStart) {
        int result = findStart ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (JavaParser.StatementContext statement : statements) {
            if (statement.getStart() != null && statement.getStop() != null) {
                int line = findStart ? statement.getStart().getLine() : statement.getStop().getLine();
                result = findStart ? Math.min(result, line) : Math.max(result, line);
            }
        }

        return findStart ? result - 1 : result;
    }
}
