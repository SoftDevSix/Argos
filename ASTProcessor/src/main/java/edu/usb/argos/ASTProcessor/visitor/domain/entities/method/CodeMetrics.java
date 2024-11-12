package edu.usb.argos.ASTProcessor.visitor.domain.entities.method;

public class CodeMetrics {
    private final int linesOfCodes;
    private final int effectiveLines;
    private final int commentLines;
    private final int emptyLines;

    public CodeMetrics(int linesOfCodes, int effectiveLines, int commentLines, int emptyLines) {
        this.linesOfCodes = linesOfCodes;
        this.effectiveLines = effectiveLines;
        this.commentLines = commentLines;
        this.emptyLines = emptyLines;
    }

    public int getLinesOfCodes() {
        return linesOfCodes;
    }

    public int getEffectiveLines() {
        return effectiveLines;
    }

    public int getCommentLines() {
        return commentLines;
    }

    public int getEmptyLines() {
        return emptyLines;
    }
}
