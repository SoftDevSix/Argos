package edu.usb.argos.ASTProcessor.visitor.application.analyzers.complexity;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.ComplexityMetrics;

public class ComplexityCalculator {
    public ComplexityMetrics calculate(JavaParser.MethodDeclarationContext ctx) {
        ComplexityVisitor visitor = new ComplexityVisitor();
        ctx.accept(visitor);
        return new ComplexityMetrics(
                visitor.getCyclomaticComplexity(),
                visitor.getMaxNestingDepth(),
                visitor.getNumberOfStatements()
        );
    }
}
