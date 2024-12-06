package edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class CodeAnalysisReport {
    private int startLine;
    private int endLine;
    private CodeAnalysisReportType type;
    private String message;
}
