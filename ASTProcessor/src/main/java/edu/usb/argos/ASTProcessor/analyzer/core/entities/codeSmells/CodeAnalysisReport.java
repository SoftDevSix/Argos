package edu.usb.argos.ASTProcessor.analyzer.core.entities.codeSmells;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CodeAnalysisReport {
    private int startLine;
    private int endLine;
    private CodeAnalysisReportType type;
    private String message;
}
