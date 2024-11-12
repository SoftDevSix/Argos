package edu.usb.argos.ASTProcessor.visitor.application.analyzers.complexity;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.ComplexityMetrics;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.IMethodAnalyzer;

public class ComplexityMetricsAnalyzer implements IMethodAnalyzer<ComplexityMetrics> {

    @Override
    public ComplexityMetrics analyze(JavaParser.MethodDeclarationContext ctx) {
        ComplexityCalculator calculator = new ComplexityCalculator();
        return calculator.calculate(ctx);
    }
}
