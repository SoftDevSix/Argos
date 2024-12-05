package edu.usb.argos.ASTProcessor.staticanalysis.analysisresult;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisReport {
    private int startLine;
    private int endLine;
    private String message;

    public AnalysisReport(int startLine, int endLine, String message) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.message = message;
    }
}
