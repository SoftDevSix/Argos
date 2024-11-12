package edu.usb.argos.ASTProcessor.visitor.domain.entities.method;

public class ComplexityMetrics {
    private final int cyclomaticComplexity;
    private final int nestingDepth;
    private final int numberOfStatements;

    public ComplexityMetrics(int cyclomaticComplexity, int nestingDepth, int numberOfStatements) {
        this.cyclomaticComplexity = cyclomaticComplexity;
        this.nestingDepth = nestingDepth;
        this.numberOfStatements = numberOfStatements;
    }

    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    public int getNestingDepth() {
        return nestingDepth;
    }

    public int getNumberOfStatements() {
        return numberOfStatements;
    }
}
