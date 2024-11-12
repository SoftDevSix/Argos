package edu.usb.argos.ASTProcessor.visitor.application.analyzers.code;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.CodeMetrics;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IMethodAnalyzer;

public class CodeMetricsAnalyzer implements IMethodAnalyzer<CodeMetrics> {
    @Override
    public CodeMetrics analyze(JavaParser.MethodDeclarationContext ctx) {
        CodeMetricsCalculator calculator = new CodeMetricsCalculator();
        return calculator.calculate(ctx);
    }
}
