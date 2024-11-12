package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

public class ClassMetrics {

    private final int totalLines;
    private final int commentLines;
    private final double commentRatio;
    private final int numberOfMethods;
    private final int numberOfAttributes;

    public ClassMetrics(int totalLines, int commentLines, double commentRatio, int numberOfMethods, int numberOfAttributes) {
        this.totalLines = totalLines;
        this.commentLines = commentLines;
        this.commentRatio = commentRatio;
        this.numberOfMethods = numberOfMethods;
        this.numberOfAttributes = numberOfAttributes;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public int getCommentLines() {
        return commentLines;
    }

    public double getCommentRatio() {
        return commentRatio;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public int getNumberOfAttributes() {
        return numberOfAttributes;
    }
}
