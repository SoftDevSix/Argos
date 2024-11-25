package edu.usb.argos.ASTProcessor.staticanalysis;

public class AnalysisReport {
    private int startLine;
    private int endLine;
    private String message;

    public AnalysisReport(int startLine, int endLine, String message) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.message = message;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
