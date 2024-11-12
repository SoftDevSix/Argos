package edu.usb.argos.ASTProcessor.visitor.application.analyzers.code;

public class MetricsCounters {
    private int effectiveLines;
    private int commentLines;
    private int emptyLines;

    public void incrementEffectiveLines() {
        effectiveLines++;
    }

    public void incrementCommentLines() {
        commentLines++;
    }

    public void incrementEmptyLines() {
        emptyLines++;
    }

    public int getLinesOfCode() {
        return effectiveLines + commentLines + emptyLines;
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
